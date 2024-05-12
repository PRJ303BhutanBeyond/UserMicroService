package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Tourist;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;
import java.io.IOException;

public interface TouristService {

    Tourist registerTourist(Tourist tourist);

    void uploadUserPhoto(int id, MultipartFile photo) throws IOException;

    Tourist findByID(int theId);

    Tourist findByEmail(String email);

    boolean isEmailUnique(String email);

    public void enable(int id, boolean enabled, String otp);

    List<Tourist> getAllTourists();

    Tourist updateTourist(Tourist tourist);

    void deleteTourist(long id);

    void updateAuthenticationType(Long touristId, AuthenticationType type);

    boolean verifyOTP(int userId, String otp);

    void addNewTouristUponOAuthLogin(String name, String email, AuthenticationType authenticationType);

    void resendOTP(String email);

}