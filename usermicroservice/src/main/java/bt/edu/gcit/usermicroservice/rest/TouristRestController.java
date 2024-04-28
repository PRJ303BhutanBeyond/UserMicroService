package bt.edu.gcit.usermicroservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.TouristService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import bt.edu.gcit.usermicroservice.entity.Role;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TouristRestController {

    private TouristService touristService;

    @Autowired
    public TouristRestController(TouristService touristService) {
        this.touristService = touristService;
    }

    @PostMapping(value = "/tourists/register", consumes = "multipart/form-data")
    public Tourist save(@RequestPart("fullName") @Valid @NotNull String fullName,
            @RequestPart("email") @Valid @NotNull String email,
            @RequestPart("Password") @Valid @NotNull String Password,
            @RequestPart("country") @Valid String country,
            @RequestPart("phoneNumber") @Valid @NotNull String phoneNumber,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        try {
            Tourist tourist = new Tourist();
            tourist.setFullName(fullName);
            tourist.setEmail(email);
            tourist.setCountry(country);
            tourist.setPhoneNumber(phoneNumber);
            tourist.setPassword(Password);

            System.out.println("Uploading photos");
            // Save the user and get the ID
            Tourist savedTourist = touristService.registerTourist(tourist);

            System.out.println("Uploading photos for user ID: " + savedTourist.getId().intValue());

            if (profilePhoto != null) {
                touristService.uploadUserPhoto(savedTourist.getId().intValue(), profilePhoto);
            }
            return savedTourist;
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photos", e);
        }
    }

    @PutMapping("/tourists/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatustourist(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody) {

        // Extract 'otp' from the request body
        String otp = requestBody.get("otp");

        if (otp == null) {
            return ResponseEntity.badRequest().body("Missing required field 'otp'.");
        }

        // Call the updateUserEnabledStatus method in the UserService
        touristService.enable(id, true, otp); // Set enabled to true

        return ResponseEntity.ok("User enabled status updated successfully.");

    }

    @PostMapping("/tourists/isEmailUnique")
    public boolean isEmailUnique(@RequestBody String email) {
        return touristService.isEmailUnique(email);
    }

    @GetMapping("/tourists/{id}")
    public Tourist getTouristById(@PathVariable int id) {
        return touristService.findByID(id);
    }

    @GetMapping("/tourists/all")
    public List<Tourist> getAllCustomers() {
        return touristService.getAllTourists();
    }

    @PutMapping("/update")
    public Tourist updateCustomer(@RequestBody Tourist tourist) {
        return touristService.updateTourist(tourist);
    }

    @DeleteMapping("/tourists/delete/{id}")
    public void deleteTourist(@PathVariable long id) {
        touristService.deleteTourist(id);
    }

}
