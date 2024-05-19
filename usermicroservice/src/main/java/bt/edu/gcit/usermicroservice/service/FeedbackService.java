package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.entity.Feedback;
import java.util.List;

public interface FeedbackService {
    Feedback save(Feedback feedback);

    void uploadPhoto(int id, MultipartFile profilePhoto) throws IOException;

    Feedback findByID(int theId);

    Feedback updateFeedback(Feedback feedback);

    List<Feedback> getAllFeedbacks();

    void deleteFeedback(long id);
}
