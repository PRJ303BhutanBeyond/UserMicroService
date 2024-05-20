package bt.edu.gcit.usermicroservice.rest;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.entity.Feedback;
import bt.edu.gcit.usermicroservice.service.FeedbackService;
import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api")
public class FeedbackRestController {

    private FeedbackService feedbackService;
    // private ImageUploadService imageUploadService;

    @Autowired
    public FeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
        // this.imageUploadService = imageUploadService;
    }

    @PostMapping(value="/feedbacks", consumes = "application/json")
    public Feedback save(@RequestBody @Valid Feedback feedback) {
        try {
            Feedback savedFeedback = feedbackService.save(feedback);
            return savedFeedback;
        } catch (Exception e) {
            throw new RuntimeException("Error during saving feedback", e);
        }
    }

    // @PostMapping(value="/feedbacks", consumes = "multipart/form-data" )
    // public Feedback save(@RequestPart("fullName") @Valid @NotNull String fullName,
    //                      @RequestPart("email") @Valid @NotNull String email,
    //                      @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
    //                      @RequestPart("message") @Valid String message) {

    //     try {
    //         Feedback feedback = new Feedback();

    //         feedback.setFullName(fullName);
    //         feedback.setEmail(email);
    //         feedback.setMessage(message);

    //         System.out.println("Saving feedback");

    //         Feedback savedFeedback = feedbackService.save(feedback);

    //         System.out.println("Saved feedback for user ID: " + savedFeedback.getId());

    //         // if (profilePhoto != null && !profilePhoto.isEmpty()) {
    //         //     System.out.println("Uploading profile photo");

    //         //     feedbackService.uploadPhoto(savedFeedback.getId().intValue(), profilePhoto);

    //         //     System.out.println("Uploaded profile photo for user ID: " + savedFeedback.getId());
    //         // }
    //         String imageUrl = imageUploadService.uploadImage(profilePhoto);
    //                        savedFeedback.setprofilePhoto(imageUrl);

    //                        // Update the user with the photo URL
    //                        feedbackService.updateFeedback(savedFeedback);


    //         return savedFeedback;
    //     } catch (Exception e) {
    //         throw new RuntimeException("Error during updating tourist details", e);
    //     }
    // }

    @GetMapping("/feedbacks/all")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/feedbacks/{id}")
    public Feedback getFeedback(@PathVariable int id) {
        return feedbackService.findByID(id);
    }

    @DeleteMapping("/feedbacks/delete/{id}")
    public void deleteFeedback(@PathVariable long id) {
        feedbackService.deleteFeedback(id);
    }


}
