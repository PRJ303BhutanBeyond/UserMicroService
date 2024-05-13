package bt.edu.gcit.usermicroservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.InvalidOtpException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findByID(int theId) {
        return entityManager.find(User.class, theId);
    }

    @Override
    public void deleteById(int theId) {
        User user = findByID(theId);
        entityManager.remove(user);
    }

    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        user.setEnabled(enabled);
        entityManager.persist(user);
    }

    @Override
    public List<User> getAllGuide(String roleName) {
        TypedQuery<User> query = entityManager.createQuery(
            "SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName", User.class);
        query.setParameter("roleName", roleName);
        return query.getResultList();
    }


    @Override
    public void updateUserEnabledStatustourist(int id, boolean enabled, String otp) {
        // Find the user by their ID
        User user = entityManager.find(User.class, id);
        // Check if the user exists
        if (user == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        // Check if the OTP provided matches the OTP in the database for the user
        if (!user.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP provided for user with id " + id);
        }
        // Set the enabled status to the provided value
        user.setEnabled(enabled);
        // Save the updated user
        entityManager.persist(user);

    }

    @Override
    public String getOTPById(int userId) {
        User user = entityManager.find(User.class, userId);
        return (user != null) ? user.getOtp() : null;
    }
}
