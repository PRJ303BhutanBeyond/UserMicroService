package bt.edu.gcit.usermicroservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.dao.TouristDAO;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import org.springframework.util.StringUtils;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.IOException;
import javax.mail.*;
import java.util.Random;

@Service
public class TouristServiceImpl implements TouristService {

    private final TouristDAO touristDAO;
    private final PasswordEncoder passwordEncoder;
    private final Random random;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
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
        int otpNumber = 100000 + random.nextInt(900000); // Generates a random 6-digit number
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
}
