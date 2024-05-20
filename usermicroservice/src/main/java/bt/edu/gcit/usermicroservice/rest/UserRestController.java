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
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
// import javax.validation.Valid;
// import javax.validation.constraints.NotNull;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.service.ImageUploadService;

import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserService userService;
    private ImageUploadService imageUploadService;
    private final UserDAO userDAO;

    @Autowired
    public UserRestController(UserService userService, UserDAO userDAO, ImageUploadService imageUploadService) {
        this.userService = userService;
        this.userDAO = userDAO;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/users", consumes = "multipart/form-data")
    public ResponseEntity<User> save(@RequestPart("fullName") @Valid @NotNull String fullName,
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

            String ProfilePhoto = imageUploadService.uploadImage(profilePhoto);
            String LicensePhoto = imageUploadService.uploadLicenseImage(licensePhoto);

            savedUser.setProfilePhoto(ProfilePhoto);
            savedUser.setLicensePhoto(LicensePhoto);
            userService.updateUser(savedUser.getId().intValue(), savedUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IOException e) {
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

    @DeleteMapping("/users/delete/{id}")
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
    @PostMapping("/users/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable int id) {
        User user = userService.disableUser(id);
        if (user == null) {
            return ResponseEntity.ok("User not found");
        }

        sendDisabledEmail(user.getEmail());

        return ResponseEntity.ok("Message successfully sent");
    }

    // Method to send OTP via email
    private void sendDisabledEmail(String recipientEmail) {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("12210097.gcit@rub.edu.bt", "ptwe rdxi trtr bbwx");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("12210097.gcit@rub.edu.bt"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Your Registration failed");
            message.setText(
                    "Dear User,\n\nWe have noticed that there might be an issue with the license number associated with your account. To ensure accurate records and compliance, we kindly request you to provide your correct license number at your earliest convenience.\n\nIf you have any questions or concerns, please feel free to contact our support team.\n\nThank you for your cooperation.\n\nBest regards,\nThe [TimberHub] Team");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            System.err.println("Error sending email: " + mex.getMessage());
            mex.printStackTrace();
        }
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
