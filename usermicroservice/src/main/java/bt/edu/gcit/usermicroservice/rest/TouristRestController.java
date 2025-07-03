package bt.edu.gcit.usermicroservice.rest;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.service.TouristService;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import org.springframework.web.multipart.MultipartFile;
import bt.edu.gcit.usermicroservice.dao.TouristDAO;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api")
public class TouristRestController {

    private TouristService touristService;
    private TouristDAO touristDAO;
    private final PasswordEncoder passwordEncoder;
    private ImageUploadService imageUploadService;
    public TouristRestController(TouristService touristService, TouristDAO touristDAO,
            PasswordEncoder passwordEncoder, ImageUploadService imageUploadService) {
        this.touristService = touristService;
        this.touristDAO = touristDAO;
        this.passwordEncoder = passwordEncoder;
        this.imageUploadService = imageUploadService;
    }


    @PostMapping(value = "/tourists/register", consumes = "multipart/form-data")
    public Tourist save(@RequestParam("fullName") @Valid @NotNull String fullName,
            @RequestParam("email") @Valid @NotNull String email,
            @RequestParam("Password") @Valid @NotNull String Password,
            @RequestParam(value = "country", required = false) @Valid String country,
            @RequestParam(value = "phoneNumber", required = false) @Valid @NotNull String phoneNumber,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
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

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                String imageUrl = imageUploadService.uploadImage(profilePhoto);
                savedTourist.setProfilePhoto(imageUrl);
                // Update the user with the photo URL
                touristService.updateTourist(savedTourist);
                // touristService.updateTouristValue(savedTourist.getId().intValue(),
                // savedTourist);
            }

            return savedTourist;
        } catch (Exception e) {
            // Handle the exception
            throw new RuntimeException("Error during registration or photo upload", e);
        }
    }

    @PutMapping(value = "/tourists/updateDetails", consumes = "multipart/form-data")
    public Tourist updateTourist(@RequestParam("email") @Valid @NotNull String email,
            @RequestParam("fullName") @Valid @NotNull String fullName,
            @RequestParam(value = "country", required = false) @Valid @NotNull String country,
            @RequestParam(value = "phoneNumber", required = false) @Valid @NotNull String phoneNumber,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        try {
            Tourist tourist = touristService.findByEmail(email);
            if (tourist == null) {
                throw new UserNotFoundException("Tourist with email " + email + " not found.");
            }

            tourist.setFullName(fullName);
            tourist.setEmail(email);
            tourist.setCountry(country);
            tourist.setPhoneNumber(phoneNumber);
            System.out.println("Uploading photos");

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                String imageUrl = imageUploadService.uploadImage(profilePhoto);
                tourist.setProfilePhoto(imageUrl);
            }

            return touristService.updateTourist(tourist);
        } catch (Exception e) {
            throw new RuntimeException("Error during updating tourist details", e);
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

    @PostMapping("/tourists/find")
    public Tourist getTouristByEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        return touristService.findByEmail(email);
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

    @GetMapping("/tourists/{id}/resend-otp")
    public String resendOTP(@PathVariable int id) {
        try {
            touristService.resendOTP(id);
            return "OTP has been resent to " + id;
        } catch (UserNotFoundException e) {
            return "User not found with email: " + id;
        } catch (Exception e) {
            return "Failed to resend OTP: " + e.getMessage();
        }
    }

    @PostMapping("/tourists/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Check if the email exists and the user is enabled
        Tourist tourist = touristDAO.findByEmail(email);
        if (tourist == null) {
            return ResponseEntity.badRequest().body("Tourist with the provided email does not exist.");
        }
        if (!tourist.isEnabled()) {
            return ResponseEntity.badRequest().body("Tourist account is not enabled.");
        }
        // Generate a new OTP
        String newOtp = generateOTP();
        // Update the OTP in the database
        tourist.setOtp(newOtp);
        touristDAO.registerTourist(tourist); // Update the user with new OTP
        // Send the new OTP to the user's email
        sendOTPEmail(tourist.getEmail(), newOtp);
        return ResponseEntity.ok("New OTP sent to the registered email address.");
    }

    private String generateOTP() {
        Random random = new Random();
        int otpNumber = 1000 + random.nextInt(9000); // Generate a random 6-digit OTP
        return String.valueOf(otpNumber);
    }

    // Method to send OTP via email
    private void sendOTPEmail(String recipientEmail, String otp) {
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
            message.setSubject("Your OTP for Verification");
            message.setText("Your OTP is: " + otp);
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            System.err.println("Error sending email: " + mex.getMessage());
            mex.printStackTrace();
        }
    }

    @PostMapping("/tourists/enterForgotPasswordOtp")
    public ResponseEntity<String> enterForgotPasswordOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String otp = requestBody.get("otp");
        String newPassword = requestBody.get("newPassword");
        // Retrieve user by email
        Tourist tourist = touristDAO.findByEmail(email);
        if (tourist == null) {
            return ResponseEntity.badRequest().body("User with the provided email does not exist.");
        }
        // Check if the OTP matches
        if (!tourist.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
        // Update the user's password
        tourist.setPassword(passwordEncoder.encode(newPassword));
        touristDAO.registerTourist(tourist); // Save the updated user
        return ResponseEntity.ok("Password updated successfully.");
    }

    
    @PostMapping("/tourists/changePassword")
    @Transactional
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String oldPassword = requestBody.get("oldPassword");
        String newPassword = requestBody.get("newPassword");

        // Check if the email exists in the database
        Tourist tourist = touristService.findByEmail(email);
        if (tourist == null) {
            return ResponseEntity.badRequest().body("Email does not exist.");
        }

        // Attempt to change the password
        try {
            touristService.changePassword(email, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("An error occurred while changing the password.");
        }
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable int id) {
        Tourist tourist = touristService.disableUser(id);
        if (tourist == null) {
            return ResponseEntity.ok("User not found");
        }
        sendDisabledEmail(tourist.getEmail());
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
                    "Dear User,\r\n" +
                            "\r\n" +
                            "We regret to inform you that your account has been disabled due to some inappropriate activity. To resolve this issue, please contact our support team at 17271244 at your earliest convenience.\r\n"
                            +
                            "\r\n" +
                            "If you have any questions or concerns, our support team is here to assist you.\r\n" +
                            "\r\n" +
                            "Thank you for your understanding.\r\n" +
                            "\r\n" +
                            "Best regards,\r\n" +
                            "The BhutanBeyond Team\r\n" +
                            "\r\n");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            System.err.println("Error sending email: " + mex.getMessage());
            mex.printStackTrace();
        }
    }

    @PutMapping("/{id}/enabled")
    public ResponseEntity<?> updateUserEnabledStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean enabled = requestBody.get("enabled");
        touristService.updateTouristEnabledStatus(id, enabled);
        System.out.println("User enabled status updated successfully");
        return ResponseEntity.ok().build();
    }
}