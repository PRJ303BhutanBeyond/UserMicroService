package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Feedback;

public interface FeedbackDAO {
    Feedback save(Feedback feedback);

    Feedback getFeedbackById(long id);
}
