package bt.edu.gcit.usermicroservice.rest;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.entity.Feedback;
import bt.edu.gcit.usermicroservice.service.FeedbackService;

@RestController
@RequestMapping("/api")
public class FeedbackRestController {

    private FeedbackService feedbackService;

    @Autowired
    public FeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping(value="/feedbacks", consumes = "multipart/form-data" )
    public Feedback save(@RequestPart("fullName") @Valid @NotNull String fullName,
                         @RequestPart("email") @Valid @NotNull String email,
                         @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
                         @RequestPart("message") @Valid String message) {

        try {
            Feedback feedback = new Feedback();

            feedback.setFullName(fullName);
            feedback.setEmail(email);
            feedback.setMessage(message);

            System.out.println("Saving feedback");

            Feedback savedFeedback = feedbackService.save(feedback);

            System.out.println("Saved feedback for user ID: " + savedFeedback.getId());

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                System.out.println("Uploading profile photo");

                feedbackService.uploadPhoto(savedFeedback.getId().intValue(), profilePhoto);

                System.out.println("Uploaded profile photo for user ID: " + savedFeedback.getId());
            }

            return savedFeedback;
        } catch (IOException e) {
            throw new RuntimeException("Error while processing feedback", e);
        }
    }
}
