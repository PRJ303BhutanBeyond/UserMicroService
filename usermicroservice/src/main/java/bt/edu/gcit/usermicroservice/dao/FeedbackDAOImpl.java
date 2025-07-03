package bt.edu.gcit.usermicroservice.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bt.edu.gcit.usermicroservice.entity.Feedback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class FeedbackDAOImpl implements FeedbackDAO {

    private EntityManager entityManager;

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
    @Transactional
    public Feedback updateFeedback(Feedback feedback) {
        return entityManager.merge(feedback);
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
