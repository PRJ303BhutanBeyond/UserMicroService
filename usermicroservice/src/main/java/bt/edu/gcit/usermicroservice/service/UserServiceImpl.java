package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.Role;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.util.StringUtils;
import java.nio.file.Path;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import java.nio.file.Paths;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Random random;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.random = new Random();
    }

    @Override
    @Transactional
    public User save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Send OTP via email
        sendOTPEmail(user.getEmail());

        // Save user with OTP
        return userDAO.save(user);
    }


    // Method to send OTP via email
    private void sendOTPEmail(String recipientEmail) {
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
            message.setSubject("Registeration Successful");
            message.setText("Great news! Your Guide account has been successfully established. Kindly await account validation by our administrative team. You can expect a confirmation email within the next 24 hours. Should you not receive it, please don't hesitate to reach out to us at either 17271244 or 1124.");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            System.err.println("Error sending email: " + mex.getMessage());
            mex.printStackTrace();
        }
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        User user = userDAO.findByEmail(email);
        return user != null;
    }

    @Override
    public User findByID(int theId) {
        return userDAO.findByID(theId);
    }


    @Override
    @Transactional
    public List<User> getAllGuide() {
        return userDAO.getAllGuide("Guide");
    }

    @Transactional
    @Override
    public User updateUser(int id, User updatedUser) {
        User existingUser = userDAO.findByID(id);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userDAO.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        userDAO.deleteById(theId);
    }

    @Override
    @Transactional
    public void updateUserEnabledStatus(int id, boolean enabled) {
        userDAO.updateUserEnabledStatus(id, enabled);
        if (enabled) {
            User user = userDAO.findByID(id);
            if (user != null) {
                sendGuideEnabledEmail(user.getEmail());
            }
        }
    }

    @Override
    @Transactional
    public void updateUserEnabledStatustourist(int id, boolean enabled, String otp) {
        userDAO.updateUserEnabledStatustourist(id, enabled, otp);
    }

    @Override
    public boolean verifyOTP(int userId, String otp) {
        String storedOTP = userDAO.getOTPById(userId);
        return otp.equals(storedOTP);
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    private void sendGuideEnabledEmail(String userEmail) {
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
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Your registration is successful");
            message.setText("Dear User,\n\nWe are happy to announce that your registration was successful!\n\n" +
                    "You are now eligible to become our tourist guide.\n\n" +
                    "Thank you for joining us!");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            System.err.println("Error sending email: " + mex.getMessage());
            mex.printStackTrace();
        }

    }

    @Transactional
    @Override
    public void uploadPhoto(int id, MultipartFile profilePhoto, MultipartFile licensePhoto) throws IOException {
        User user = findByID(id);

        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            uploadProfilePhoto(profilePhoto, user);
        }

        if (licensePhoto != null && !licensePhoto.isEmpty()) {
            uploadLicensePhoto(licensePhoto, user);
        }
    }

    private void uploadProfilePhoto(MultipartFile profilePhoto, User user) throws IOException {
        if (profilePhoto.getSize() > 1024 * 1024) {
            throw new FileSizeException("Profile photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(profilePhoto.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        profilePhoto.transferTo(uploadPath);
        user.setProfilePhoto(filename);
    }

    private void uploadLicensePhoto(MultipartFile licensePhoto, User user) throws IOException {
        if (licensePhoto.getSize() > 1024 * 1024) {
            throw new FileSizeException("License photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(licensePhoto.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        licensePhoto.transferTo(uploadPath);
        user.setLicensePhoto(filename);
    }

    @Transactional
    @Override
    public User disableUser(int id) {
        User user = userDAO.findByID(id);
        if (user != null) {
            user.setEnabled(false);
            userDAO.save(user);
        }
        return user;
    }

    @Override
    public void sendGuideEnabledEmail(String to, String subject, String body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendEmail'");
    }

}
