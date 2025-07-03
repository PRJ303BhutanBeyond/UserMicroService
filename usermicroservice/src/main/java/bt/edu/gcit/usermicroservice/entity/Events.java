package bt.edu.gcit.usermicroservice.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "event")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Column(name = "location", nullable = false, length = 45)
    private String location;
    @Column(name = "startingDate", nullable = false, length = 45)
    private String startingDate;
    @Column(name = "endingDate", nullable = false, length = 45)
    private String endingDate;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String photo;
    // Constructors
    public Events() {
        // Empty constructor
    }

    public Events(String name, String location, String startingDate, String endingDate, String description) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startingDate =startingDate;
        this.endingDate = endingDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
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
