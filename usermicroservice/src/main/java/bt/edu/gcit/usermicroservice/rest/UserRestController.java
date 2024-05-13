package bt.edu.gcit.usermicroservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.service.UserService;

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
// import javax.validation.Valid;
// import javax.validation.constraints.NotNull;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.Tourist;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public User save(@RequestPart("fullName") @Valid @NotNull String fullName,
            @RequestPart("email") @Valid @NotNull String email,
            @RequestPart("Password") @Valid @NotNull String Password,
            @RequestPart("address") @Valid String address,
            @RequestPart("phoneNumber") @Valid @NotNull String phoneNumber,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
            @RequestPart(value = "licensePhoto", required = false) MultipartFile licensePhoto,
            @RequestPart("roles") @Valid @NotNull String rolesJson) {
        try {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(Password);

            ObjectMapper objectMapper = new ObjectMapper();

            Set<Role> roles = objectMapper.readValue(rolesJson, new TypeReference<Set<Role>>() {
            });

            user.setRoles(roles);
            System.out.println("Uploading photos");
            // Save the user and get the ID
            User savedUser = userService.save(user);

            System.out.println("Uploading photos for user ID: " + savedUser.getId().intValue());

            if (profilePhoto != null || licensePhoto != null) {
                userService.uploadPhoto(savedUser.getId().intValue(), profilePhoto, licensePhoto);
            }

            return savedUser;
        } catch (IOException e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photos", e);
        }
    }

    @GetMapping("/users/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }

    /**
     * Updates a user with the given ID using the provided User object.
     *
     * @param id          the ID of the user to be updated
     * @param updatedUser the User object containing the updated information
     * @return the updated User object
     */

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @GetMapping("/users/guide/all")
    public List<User> getAllGuide() {
        return userService.getAllGuide();
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    /**
     * Update the enabled status of a user with the specified id
     *
     * @param id      The ID of the user to update
     * @param enabled The new enabled status
     * @return OK if the update was successful
     */
    @PutMapping("/users/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean enabled = requestBody.get("enabled");
        userService.updateUserEnabledStatus(id, enabled);
        System.out.println("User enabled status updated successfully");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/enabled/guide")
    public ResponseEntity<?> updateUserEnabledStatustourist(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody) {

        // Extract 'otp' from the request body
        String otp = requestBody.get("otp");

        if (otp == null) {
            return ResponseEntity.badRequest().body("Missing required field 'otp'.");
        }

        // Call the updateUserEnabledStatus method in the UserService
        userService.updateUserEnabledStatustourist(id, true, otp); // Set enabled to true

        return ResponseEntity.ok("User enabled status updated successfully.");

    }

}
