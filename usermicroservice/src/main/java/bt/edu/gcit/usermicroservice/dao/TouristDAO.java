package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Tourist;


import java.util.List;


import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

public interface TouristDAO {

    Tourist registerTourist(Tourist tourist);

    public Tourist findByEmail(String email);

    public Tourist getTouristByEmail(String email);

    public void enable(int id, boolean enabled, String otp);

    boolean isEmailUnique(String email);

    Tourist getTouristById(long id);

    List<Tourist> getAllTourists();

    Tourist updateTourist(Tourist tourist);

    Tourist update(long id, Tourist tourist);

    void deleteTourist(long id);

    String getOTPById(int touristId);

    void updateAuthenticationType(Long touristId,AuthenticationType type);

    void updateTouristEnabledStatus(int id, boolean enabled);

}