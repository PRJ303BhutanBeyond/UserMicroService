package bt.edu.gcit.usermicroservice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "package_name", length = 128, nullable = false)
    private String packageName;

    @Column(name = "package_type", length = 64, nullable = false)
    private String packageType;

    @Column(length = 128, nullable = false)
    private String email;

    @Column(name = "full_name", length = 45, nullable = false)
    private String fullName;

    @Column(name = "guide_email", length = 128, nullable = true)
    private String guide;

    @Column(name = "guide_assign_date", length = 128, nullable = true)
    private String date;

    @Column(name = "country", length = 64)
    private String country;

    @Column(name = "No._of_travelers")
    private int travelers;

    @Column(name = "date_of_travel", length = 64)
    private String travelDate;

    @Column(name = "phoneNo", length = 64)
    private String phone;

    @Column(name = "total_amount")
    private int amount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String information;

    @Column(nullable = true)
    private boolean paid;

    @Column(nullable = true)
    private boolean assign;

    @Column(nullable = true)
    private boolean done;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public Booking() {
        this.paid = false;
        this.assign=false;
        this.done= false;
        this.createdTime = LocalDateTime.now();
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String gettravelDate() {
        return travelDate;
    }

    public void settravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public int getTravelers() {
        return travelers;
    }

    public void setTraveler(int travelers) {
        this.travelers = travelers;
    }

    public int getAmount() {
        return amount;
    }

    public void setPrice(int amount) {
        this.amount = amount;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean getAssign() {
        return assign;
    }

    public void setAssign(boolean assign) {
        this.assign = assign;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
