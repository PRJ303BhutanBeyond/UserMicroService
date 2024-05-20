package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

public interface UserService {
    
    User save(User user);

    boolean isEmailDuplicate(String email);

    User updateUser(int id, User updatedUser);

    void deleteById(int theId);

    void updateUserEnabledStatus(int id, boolean enabled);

    void updateUserEnabledStatustourist(int id, boolean enabled, String otp);

    void sendGuideEnabledEmail(String to, String subject, String body);

    boolean verifyOTP(int userId, String otp);

    void uploadPhoto(int id, MultipartFile profilePhoto, MultipartFile licensePhoto) throws IOException;

    User findByID(int theId);

    User findByEmail(String email);

    List<User> getAllGuide();

    User disableUser(int id);
}
