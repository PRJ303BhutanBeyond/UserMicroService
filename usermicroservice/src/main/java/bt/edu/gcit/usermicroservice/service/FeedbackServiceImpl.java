package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import bt.edu.gcit.usermicroservice.dao.FeedbackDAO;
import bt.edu.gcit.usermicroservice.entity.Feedback;
import bt.edu.gcit.usermicroservice.entity.Tourist;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackDAO feedbackDAO;
    private final String uploadDir = "src/main/resources/static/feedbackimages";

    @Autowired
    @Lazy
    public FeedbackServiceImpl(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    @Override
    @Transactional
    public Feedback save(Feedback feedback) {
        return feedbackDAO.save(feedback);
    }

    @Override
    public Feedback findByID(int theId) {
        return feedbackDAO.getFeedbackById(theId);
    }

    @Transactional
    @Override
    public void uploadPhoto(int id, MultipartFile photo) throws IOException {
        Feedback feedback = findByID(id);
        if (feedback == null) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        if (photo.getSize() > 1024 * 1024) {
            throw new FileSizeException("File size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0,
                originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Append the timestamp to the filename
        String filename = filenameWithoutExtension + "_" + timestamp + "." +
                filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        feedback.setprofilePhoto(filename);
        feedbackDAO.save(feedback);
    }

    @Override
    @Transactional
    public List<Feedback> getAllFeedbacks() {
        return feedbackDAO.getAllFeedbacks();
    }

    @Override
    @Transactional
    public void deleteFeedback(long id) {
        feedbackDAO.deleteFeedback(id);
    }
}
