package bt.edu.gcit.usermicroservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.dao.TouristDAO;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import org.springframework.util.StringUtils;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.Date;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import javax.mail.*;
import java.util.Random;

@Service
public class TouristServiceImpl implements TouristService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TouristDAO touristDAO;
    private final PasswordEncoder passwordEncoder;
    private final Random random;
    private final String uploadDir = "src/main/resources/static/images";
    public TouristServiceImpl(TouristDAO touristDAO, PasswordEncoder passwordEncoder) {
        this.touristDAO = touristDAO;
        this.passwordEncoder = passwordEncoder;
        this.random = new Random();
    }

    @Override
    @Transactional
    public Tourist registerTourist(Tourist tourist) {
        tourist.setPassword(passwordEncoder.encode(tourist.getPassword()));

        String otp = generateOTP();

        // Set OTP for the user
        tourist.setOtp(otp);

        // Send OTP via email
        sendOTPEmail(tourist.getEmail(), otp);

        return touristDAO.registerTourist(tourist);
    }

    // Generate a 6-digit OTP
    private String generateOTP() {
        int otpNumber = 1000 + random.nextInt(9000); // Generates a random 6-digit number
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

    @Override
    public Tourist findByID(int theId) {
        return touristDAO.getTouristById(theId);
    }

    @Transactional
    @Override
    public void uploadUserPhoto(int id, MultipartFile photo) throws IOException {
        Tourist tourist = findByID(id);
        if (tourist == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0,
                originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Append the timestamp to the filename
        String filename = filenameWithoutExtension + "_" + timestamp + "." +
                filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        tourist.setProfilePhoto(filename);
        touristDAO.registerTourist(tourist);
    }

    @Override
    @Transactional
    public Tourist findByEmail(String email) {
        return touristDAO.findByEmail(email);
    }

    @Override
    @Transactional
    public Tourist getTouristByEmail(String email) {
        return touristDAO.getTouristByEmail(email);
    }

    @Override
    @Transactional
    public void enable(int id, boolean enabled, String otp) {
        touristDAO.enable(id, enabled, otp);
    }

    @Override
    @Transactional
    public boolean isEmailUnique(String email) {
        return touristDAO.isEmailUnique(email);
    }

    @Override
    public boolean verifyOTP(int userId, String otp) {
        String storedOTP = touristDAO.getOTPById(userId);
        return otp.equals(storedOTP);
    }

    @Override
    @Transactional
    public List<Tourist> getAllTourists() {
        return touristDAO.getAllTourists();
    }

    @Override
    @Transactional
    public Tourist updateTourist(Tourist tourist) {
        return touristDAO.updateTourist(tourist);
    }

    @Override
    @Transactional
    public void deleteTourist(long id) {
        touristDAO.deleteTourist(id);
    }

    @Override
    @Transactional
    public void updateAuthenticationType(Long touristId, AuthenticationType type) {
        touristDAO.updateAuthenticationType(touristId, type);
    }

    @Override
    @Transactional
    public void addNewTouristUponOAuthLogin(String name, String email,
            AuthenticationType authenticationType) {
        Tourist tourist = new Tourist();
        tourist.setEmail(email);
        // customer.setFirstName(name);
        setName(name, tourist);
        tourist.setEnabled(true);
        tourist.setCreatedTime(new Date());
        tourist.setAuthenticationType(authenticationType);
        tourist.setPassword("");
        tourist.setPhoneNumber("");
        tourist.setCountry("");
        System.out.println("Tourist: " + tourist);
        touristDAO.registerTourist(tourist);

    }

    private void setName(String name, Tourist tourist) {
        String[] names = name.split(" ");
        if (names.length < 2) {
            tourist.setFullName(names[0]);
        } else {
            String firstName = names[0];
            tourist.setFullName(firstName);
        }
    }

    @Override
    @Transactional
    public void resendOTP(int id) {
        Tourist tourist = findByID(id);
        if (tourist == null) {
            throw new UserNotFoundException("User not found with email: " + id);
        }

        String newOTP = generateOTP1();
        tourist.setOtp(newOTP);

        entityManager.persist(tourist);

        sendOTPEmail1(tourist.getEmail(), newOTP);
    }

    private String generateOTP1() {
        int otpNumber = 1000 + random.nextInt(9000); // Generates a random 6-digit number
        return String.valueOf(otpNumber);
    }

    private void sendOTPEmail1(String recipientEmail, String otp) {
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

    @Transactional
    @Override
    public Tourist updateTouristValue(long id, Tourist updatedUser) {
        // First, find the user by ID
        Tourist existingUser = touristDAO.getTouristById(id);
        // If the user doesn't exist, throw UserNotFoundException
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        // Update the existing user with the data from updatedUser
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setCountry(updatedUser.getCountry());
        // Check if the password has changed. If it has, encode the new password before
        // saving.
        if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        // Save the updated user and return it
        return touristDAO.registerTourist(existingUser);
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        Tourist tourist = touristDAO.findByEmail(email);
        if (tourist == null) {
            throw new UserNotFoundException("User with the provided email does not exist.");
        }
        if (!passwordEncoder.matches(oldPassword, tourist.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        tourist.setPassword(passwordEncoder.encode(newPassword));
        touristDAO.registerTourist(tourist);
    }

    @Transactional
    @Override
    public Tourist disableUser(int id) {
        Tourist tourist = touristDAO.getTouristById(id);
        if (tourist != null) {
            tourist.setEnabled(false);
            touristDAO.registerTourist(tourist);
        }
        return tourist;
    }

    @Transactional
    @Override
    public void updateTouristEnabledStatus(int id, boolean enabled) {
        touristDAO.updateTouristEnabledStatus(id, enabled);
        if (enabled) {
            Tourist tourist = touristDAO.getTouristById(id);
            if (tourist != null) {
                sendEnabledEmail(tourist.getEmail());
            }
        }
    }

    private void sendEnabledEmail(String userEmail) {
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
            message.setSubject("Account Enabled Successfully");
            message.setText(
                "Dear User,\n\n" +
                "We are pleased to inform you that your account has been successfully enabled. You can now log in and access all the features of our service.\n\n" +
                "If you have any questions or need further assistance, please do not hesitate to contact our support team at 17271244.\n\n" +
                "Thank you for your patience and understanding.\n\n" +
                "Best regards,\n" +
                "The BhuatnBeyond Team\n"
            );
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            System.err.println("Error sending email: " + mex.getMessage());
            mex.printStackTrace();
        }

    }
}