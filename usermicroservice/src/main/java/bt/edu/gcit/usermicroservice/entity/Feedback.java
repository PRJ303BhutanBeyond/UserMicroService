package bt.edu.gcit.usermicroservice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false)
    private String email;

    @Column(name = "full_name", length = 45, nullable = false)
    private String fullName;

    @Column(name = "profile_photo", length = 64)
    private String profilePhoto;

    @Column(nullable = false)
    private String message;

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    public Feedback() {
        this.createdTime = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getprofilePhoto() {
        return profilePhoto;
    }

    public void setprofilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
