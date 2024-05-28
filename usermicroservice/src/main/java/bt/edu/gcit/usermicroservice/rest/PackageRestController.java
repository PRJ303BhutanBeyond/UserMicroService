package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.TourPackage;
import bt.edu.gcit.usermicroservice.service.PackageService;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api")
public class PackageRestController {

    private PackageService packageService;
    private ImageUploadService imageUploadService;

    @Autowired
    public PackageRestController(PackageService packageService, ImageUploadService imageUploadService) {
        this.packageService = packageService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/packages", consumes = "multipart/form-data")
    public ResponseEntity<TourPackage> save(@RequestParam("topic") String topic,
            @RequestParam("type") String type,
            @RequestParam("overview") String overview,
            @RequestParam("price") String price,
            @RequestParam("durationString") String durationString,
            @RequestParam("Location") String Location,
            @RequestParam("preferredMonth") String preferredMonth,
            @RequestParam("highlight") String highlight,
            @RequestParam("dailyItinerary") String dailyItinerary,
            @RequestParam("included") String included,
            @RequestParam("excluded") String excluded,
            @RequestParam(value = "photo_1", required = false) MultipartFile photo_1,
            @RequestParam(value = "photo_2", required = false) MultipartFile photo_2,
            @RequestParam(value = "photo_3", required = false) MultipartFile photo_3,
            @RequestParam(value = "photo_4", required = false) MultipartFile photo_4,
            @RequestParam(value = "photo_5", required = false) MultipartFile photo_5) {
        try {
            TourPackage tourPackage = new TourPackage();
            tourPackage.setTopic(topic);
            tourPackage.setType(type);
            tourPackage.setOverview(overview);
            tourPackage.setPrice(price);
            tourPackage.setLocation(Location);
            tourPackage.setDurationString(durationString);
            tourPackage.setPreferredMonth(preferredMonth);
            tourPackage.setHighlight(highlight);
            tourPackage.setIncluded(included);
            tourPackage.setExcluded(excluded);
            tourPackage.setDailyItinerary(dailyItinerary);

            System.out.println("Uploading photos");

            // Save the tour package
            TourPackage savedTourPackage = packageService.save(tourPackage);

            System.out.println("Uploading photos for user ID: " + savedTourPackage.getId());

            String imageUrl1 = imageUploadService.uploadImage1(photo_1);
            String imageUrl2 = imageUploadService.uploadImage2(photo_2);
            String imageUrl3 = imageUploadService.uploadImage3(photo_3);
            String imageUrl4 = imageUploadService.uploadImage4(photo_4);
            String imageUrl5 = imageUploadService.uploadImage5(photo_5);

            savedTourPackage.setPhoto1(imageUrl1);
            savedTourPackage.setPhoto2(imageUrl2);
            savedTourPackage.setPhoto3(imageUrl3);
            savedTourPackage.setPhoto4(imageUrl4);
            savedTourPackage.setPhoto5(imageUrl5);
            packageService.updatePackage(savedTourPackage.getId().intValue(), savedTourPackage);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTourPackage);
        } catch (Exception e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photos", e);
        }
    }

    @GetMapping("/packages/{id}")
    public TourPackage findByID(@PathVariable int id) {
        return packageService.findByID(id);
    }

    @GetMapping("/packages/all")
    public List<TourPackage> getAllTourPackages() {
        return packageService.getAllTourPackages();
    }

    @PutMapping(value = "/packages/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<TourPackage> updatePackage(@PathVariable int id,
            @RequestParam("topic") String topic,
            @RequestParam("type") String type,
            @RequestParam("overview") String overview,
            @RequestParam("price") String price,
            @RequestParam("durationString") String durationString,
            @RequestParam("Location") String Location,
            @RequestParam("preferredMonth") String preferredMonth,
            @RequestParam("highlight") String highlight,
            @RequestParam("dailyItinerary") String dailyItinerary,
            @RequestParam("included") String included,
            @RequestParam("excluded") String excluded,
            @RequestParam(value = "photo_1", required = false) MultipartFile photo_1,
            @RequestParam(value = "photo_2", required = false) MultipartFile photo_2,
            @RequestParam(value = "photo_3", required = false) MultipartFile photo_3,
            @RequestParam(value = "photo_4", required = false) MultipartFile photo_4,
            @RequestParam(value = "photo_5", required = false) MultipartFile photo_5) {
        try {
            // Fetch the existing TourPackage
            TourPackage existingTourPackage = packageService.findByID(id);
            if (existingTourPackage == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Update the fields
            existingTourPackage.setTopic(topic);
            existingTourPackage.setType(type);
            existingTourPackage.setOverview(overview);
            existingTourPackage.setPrice(price);
            existingTourPackage.setLocation(Location);
            existingTourPackage.setDurationString(durationString);
            existingTourPackage.setPreferredMonth(preferredMonth);
            existingTourPackage.setHighlight(highlight);
            existingTourPackage.setIncluded(included);
            existingTourPackage.setExcluded(excluded);
            existingTourPackage.setDailyItinerary(dailyItinerary);

            // Upload photos if they are present
            if (photo_1 != null && !photo_1.isEmpty()) {
                String imageUrl1 = imageUploadService.uploadImage1(photo_1);
                existingTourPackage.setPhoto1(imageUrl1);
            }
            if (photo_2 != null && !photo_2.isEmpty()) {
                String imageUrl2 = imageUploadService.uploadImage2(photo_2);
                existingTourPackage.setPhoto2(imageUrl2);
            }
            if (photo_3 != null && !photo_3.isEmpty()) {
                String imageUrl3 = imageUploadService.uploadImage3(photo_3);
                existingTourPackage.setPhoto3(imageUrl3);
            }
            if (photo_4 != null && !photo_4.isEmpty()) {
                String imageUrl4 = imageUploadService.uploadImage4(photo_4);
                existingTourPackage.setPhoto4(imageUrl4);
            }
            if (photo_5 != null && !photo_5.isEmpty()) {
                String imageUrl5 = imageUploadService.uploadImage5(photo_5);
                existingTourPackage.setPhoto5(imageUrl5);
            }

            // Save the updated package
            packageService.updatePackage(id, existingTourPackage);

            return ResponseEntity.status(HttpStatus.OK).body(existingTourPackage);

        } catch (Exception e) {
            // Log the exception and return a meaningful error message;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/packages/delete/{id}")
    public void deleteById(@PathVariable int id) {
        packageService.deleteById(id);
    }
}
