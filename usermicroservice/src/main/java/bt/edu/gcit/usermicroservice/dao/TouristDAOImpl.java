package bt.edu.gcit.usermicroservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.InvalidOtpException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

import bt.edu.gcit.usermicroservice.entity.AuthenticationType;

@Repository
public class TouristDAOImpl implements TouristDAO {

    private EntityManager entityManager;

    @Autowired
    public TouristDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Tourist registerTourist(Tourist tourist) {
        tourist.setAuthenticationType(AuthenticationType.DATABASE);
        entityManager.persist(tourist);
        return tourist;
    }

    @Override
    public Tourist findByEmail(String email) {
        TypedQuery<Tourist> query = entityManager.createQuery(
                "SELECT t FROM Tourist t WHERE t.email = :email", Tourist.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    @Override
    public Tourist getTouristByEmail(String email) {
        return entityManager.find(Tourist.class, email);
    }

    @Override
    @Transactional
    public void enable(int id, boolean enabled, String otp) {
        Tourist tourist = entityManager.find(Tourist.class, id);
        if (tourist == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        // Check if the OTP provided matches the OTP in the database for the user
        if (!tourist.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP provided for user with id " + id);
        }
        // Set the enabled status to the provided value
        tourist.setEnabled(enabled);
        // Save the updated user
        entityManager.persist(tourist);
    }

    @Override
    public boolean isEmailUnique(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(t) FROM Tourist t WHERE t.email = :email", Long.class);
        query.setParameter("email", email);
        long count = query.getSingleResult();
        return count == 0;
    }

    @Override
    public Tourist getTouristById(long id) {
        return entityManager.find(Tourist.class, id);
    }

    @Override
    public List<Tourist> getAllTourists() {
        TypedQuery<Tourist> query = entityManager.createQuery(
                "SELECT t FROM Tourist t", Tourist.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Tourist updateTourist(Tourist Tourist) {
        return entityManager.merge(Tourist);
    }

    @Override
    public void deleteTourist(long id) {
        Tourist tourist = entityManager.find(Tourist.class, id);
        if (tourist != null) {
            entityManager.remove(tourist);
        }
    }

    @Override
    public void updateAuthenticationType(Long touristId, AuthenticationType type) {
        Tourist tourist = entityManager.find(Tourist.class, touristId);
        if (tourist != null) {
            tourist.setAuthenticationType(type);
            entityManager.merge(tourist);
        }
    }

    @Override
    @Transactional
    public String getOTPById(int touristId) {
        Tourist tourist = entityManager.find(Tourist.class, touristId);
        return (tourist != null) ? tourist.getOtp() : null;
    }

}