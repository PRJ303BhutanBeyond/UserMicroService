package bt.edu.gcit.usermicroservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import bt.edu.gcit.usermicroservice.entity.Feedback;

import jakarta.persistence.EntityManager;

@Repository
public class FeedbackDAOImpl implements FeedbackDAO {

    private EntityManager entityManager;

    @Autowired
    public FeedbackDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Feedback save(Feedback feedback){
        entityManager.persist(feedback);
        return feedback;
    }

    @Override
    public Feedback getFeedbackById(long id) {
        return entityManager.find(Feedback.class, id);
    }
}

