package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "topdestination")
public class TopDestination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(length = 500)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "dzongkhag_name", nullable = false)
    private Dzongkhag dzongkhag;

    // Constructors
    public TopDestination() {
        // Empty constructor
    }

    public TopDestination(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Dzongkhag getDzongkhag() {
        return dzongkhag;
    }

    public void setDzongkhag(Dzongkhag dzongkhag) {
        this.dzongkhag = dzongkhag;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
