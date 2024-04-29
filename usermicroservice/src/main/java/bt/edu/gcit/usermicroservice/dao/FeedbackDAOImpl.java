package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import bt.edu.gcit.usermicroservice.entity.Feedback;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class FeedbackDAOImpl implements FeedbackDAO {

    private EntityManager entityManager;

    @Autowired
    public FeedbackDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Feedback save(Feedback feedback) {
        entityManager.persist(feedback);
        return feedback;
    }

    @Override
    public Feedback getFeedbackById(long id) {
        return entityManager.find(Feedback.class, id);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        TypedQuery<Feedback> query = entityManager.createQuery(
                "SELECT f FROM Feedback f", Feedback.class);
        return query.getResultList();
    }

    @Override
    public void deleteFeedback(long id) {
        Feedback feedback = entityManager.find(Feedback.class, id);
        if (feedback != null) {
            entityManager.remove(feedback);
        }
    }
}
