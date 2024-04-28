package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.User;

public interface UserDAO {
    User save(User user);

    User findByEmail(String email);

    User findByID(int theId);

    void deleteById(int theId);

    void updateUserEnabledStatus(int id, boolean enabled);

    void updateUserEnabledStatustourist(int id, boolean enabled, String otp);

    String getOTPById(int userId);

}