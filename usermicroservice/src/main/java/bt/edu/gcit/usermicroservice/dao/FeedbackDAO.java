package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Feedback;
import java.util.List;

public interface FeedbackDAO {
    Feedback save(Feedback feedback);

    Feedback getFeedbackById(long id);
    
    List<Feedback> getAllFeedbacks();

    void deleteFeedback(long id);
}
