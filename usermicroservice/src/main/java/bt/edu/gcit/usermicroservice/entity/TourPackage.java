package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TourPackage")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String overview;

    @Column(nullable = false)
    private String price;

    @Column(name = "Location", nullable = false)
    private String Location; 

    @Column(name = "duration", nullable = false)
    private String duration; // Duration as a String

    @Column(name = "preferred_month")
    private String preferredMonth;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String highlight;

    @Column(name = "daily_itinerary", columnDefinition = "TEXT")
    private String dailyItinerary;

    @Column(columnDefinition = "TEXT")
    private String included;

    @Column(columnDefinition = "TEXT")
    private String excluded;

    @Column(name = "photo_1", length = 500)
    private String photo_1;

    @Column(name = "photo_2", length = 500)
    private String photo_2;

    @Column(name = "photo_3", length = 500)
    private String photo_3;

    @Column(name = "photo_4", length = 500)
    private String photo_4;

    @Column(name = "photo_5", length = 500)
    private String photo_5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDurationString() {
        return duration;
    }

    public void setDurationString(String duration) {
        this.duration = duration;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPreferredMonth() {
        return preferredMonth;
    }

    public void setPreferredMonth(String preferredMonth) {
        this.preferredMonth = preferredMonth;
    }

    public String getPhoto1() {
        return photo_1;
    }

    public void setPhoto1(String photo_1) {
        this.photo_1 = photo_1;
    }

    public String getPhoto2() {
        return photo_2;
    }

    public void setPhoto2(String photo_2) {
        this.photo_2 = photo_2;
    }

    public String getPhoto3() {
        return photo_3;
    }

    public void setPhoto3(String photo_3) {
        this.photo_3 = photo_3;
    }

    public String getPhoto4() {
        return photo_4;
    }

    public void setPhoto4(String photo_4) {
        this.photo_4 = photo_4;
    }

    public String getPhoto5() {
        return photo_5;
    }

    public void setPhoto5(String photo_5) {
        this.photo_5 = photo_5;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getDailyItinerary() {
        return dailyItinerary;
    }

    public void setDailyItinerary(String dailyItinerary) {
        this.dailyItinerary = dailyItinerary;
    }

    public String getIncluded() {
        return included;
    }

    public void setIncluded(String included) {
        this.included = included;
    }

    public String getExcluded() {
        return excluded;
    }

    public void setExcluded(String excluded) {
        this.excluded = excluded;
    }
}