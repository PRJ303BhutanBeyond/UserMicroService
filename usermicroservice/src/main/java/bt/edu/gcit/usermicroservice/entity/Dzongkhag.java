package bt.edu.gcit.usermicroservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "dzongkhags")
public class Dzongkhag {
    public Dzongkhag() {
        // Default constructor
    }

    public Dzongkhag(String dzongkhagName, String subtitle, String description, String route, String temperature) {
        this.dzongkhagName = dzongkhagName;
        this.subtitle = subtitle;
        this.description = description;
        this.route = route;
        this.temperature = temperature;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "dzongkhag_name", nullable = false, unique = true, length = 45)
    private String dzongkhagName;
    @Column(name = "subtitle", nullable = true, length = 45)
    private String subtitle;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "route", nullable = false, columnDefinition = "TEXT")
    private String route;
    @Column(name = "temperature", nullable = false, columnDefinition = "TEXT")
    private String temperature;

    @Column(length = 500)
    private String photo1;

    @Column(length = 500)
    private String photo2;

    @Column(length = 500)
    private String photo3;

    @Column(length = 500)
    private String photo4;

    @Column(length = 500)
    private String photo5;

    @OneToMany(mappedBy = "dzongkhag", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<TopDestination> destinations;

    public List<TopDestination> getDestinations() {
        return destinations;
    }
 
    public void setDestinations(List<TopDestination> destinations) {
        this.destinations = destinations;
    }
 

    // Getters
    public int getId() {
        return id;
    }

    public String getdzongkhagName() {
        return dzongkhagName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getRoute() {
        return route;
    }

    public String getTemperature() {
        return temperature;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setdzongkhagName(String dzongkhagName) {
        this.dzongkhagName = dzongkhagName;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

}