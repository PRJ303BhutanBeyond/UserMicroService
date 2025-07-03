package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import bt.edu.gcit.usermicroservice.service.DzongkhagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api/dzongkhags")
public class DzongkhagRestController {
    private DzongkhagService dzongkhagService;
    private ImageUploadService imageUploadService;
    public DzongkhagRestController(DzongkhagService thedzongkhagService, ImageUploadService imageUploadService) {
        dzongkhagService = thedzongkhagService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<Dzongkhag> save(@RequestPart("dzongkhagName") @Valid @NotNull String dzongkhagName,
            @RequestPart("subtitle") @Valid String subtitle,
            @RequestPart("description") @Valid @NotNull String description,
            @RequestPart("route") @Valid @NotNull String route,
            @RequestPart("temperature") @Valid @NotNull String temperature,
            @RequestPart(value = "photo1", required = false) MultipartFile photo1,
            @RequestPart(value = "photo2", required = false) MultipartFile photo2,
            @RequestPart(value = "photo3", required = false) MultipartFile photo3,
            @RequestPart(value = "photo4", required = false) MultipartFile photo4,
            @RequestPart(value = "photo5", required = false) MultipartFile photo5) {
        try {
            Dzongkhag dzongkhag = new Dzongkhag();
            dzongkhag.setdzongkhagName(dzongkhagName);
            dzongkhag.setSubtitle(subtitle);
            dzongkhag.setDescription(description);
            dzongkhag.setRoute(route);
            dzongkhag.setTemperature(temperature);
            System.out.println("Uploading photos");
            // Save the user and get the ID
            Dzongkhag savedDzongkhag = dzongkhagService.save(dzongkhag);

            System.out.println("Uploading photos for Dzongkhag ID: " + savedDzongkhag.getId());

            String imageUrl1 = imageUploadService.uploadImage1(photo1);
            String imageUrl2 = imageUploadService.uploadImage2(photo2);
            String imageUrl3 = imageUploadService.uploadImage3(photo3);
            String imageUrl4 = imageUploadService.uploadImage4(photo4);
            String imageUrl5 = imageUploadService.uploadImage5(photo5);

            savedDzongkhag.setPhoto1(imageUrl1);
            savedDzongkhag.setPhoto2(imageUrl2);
            savedDzongkhag.setPhoto3(imageUrl3);
            savedDzongkhag.setPhoto4(imageUrl4);
            savedDzongkhag.setPhoto5(imageUrl5);

            dzongkhagService.update(savedDzongkhag.getId(), savedDzongkhag);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedDzongkhag);
        } catch (Exception e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photos", e);
        }
    }


    @GetMapping("/{dzongkhagName}")
   public ResponseEntity<Dzongkhag> getDzongkhag(@PathVariable String dzongkhagName) {
       Dzongkhag dzongkhag = dzongkhagService.findByNameDzongkhag(dzongkhagName);
       return new ResponseEntity<>(dzongkhag, HttpStatus.OK);
   }

   @GetMapping
   public ResponseEntity<List<Dzongkhag>> getAllDzongkhags() {
       List<Dzongkhag> dzongkhags = dzongkhagService.findAll();
       return new ResponseEntity<>(dzongkhags, HttpStatus.OK);
   }

   @GetMapping("/ordered")
   public ResponseEntity<List<Dzongkhag>> getAllDzongkhagsOrdered() {
       List<Dzongkhag> dzongkhags = dzongkhagService.findAllByOrderByNameAsc();
       return new ResponseEntity<>(dzongkhags, HttpStatus.OK);
   }

   @PutMapping
   public ResponseEntity<Dzongkhag> updateDzongkhag(@RequestBody Dzongkhag dzongkhag) {
       dzongkhagService.update(dzongkhag);
       return new ResponseEntity<>(dzongkhag, HttpStatus.OK);
   }

   @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
   public ResponseEntity<Dzongkhag> updatePackage(@PathVariable int id,
           @RequestParam("dzongkhagName") String dzongkhagName,
           @RequestParam("subtitle") String subtitle,
           @RequestParam("description") String description,
           @RequestParam("route") String route,
           @RequestParam("temperature") String temperature,
           @RequestParam(value = "photo1", required = false) MultipartFile photo1,
           @RequestParam(value = "photo2", required = false) MultipartFile photo2,
           @RequestParam(value = "photo3", required = false) MultipartFile photo3,
           @RequestParam(value = "photo4", required = false) MultipartFile photo4,
           @RequestParam(value = "photo5", required = false) MultipartFile photo5) {
       try {
           // Fetch the existing TourPackage
           Dzongkhag existingDzongkhag = dzongkhagService.findByID(id);
           if (existingDzongkhag == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
           }

           // Update the fields
           existingDzongkhag.setdzongkhagName(dzongkhagName);
           existingDzongkhag.setSubtitle(subtitle);
           existingDzongkhag.setDescription(description);
           existingDzongkhag.setRoute(route);
           existingDzongkhag.setTemperature(temperature);

           // Upload photos if they are present
           if (photo1 != null && !photo1.isEmpty()) {
               String imageUrl1 = imageUploadService.uploadImage1(photo1);
               existingDzongkhag.setPhoto1(imageUrl1);
           }
           if (photo2 != null && !photo2.isEmpty()) {
               String imageUrl2 = imageUploadService.uploadImage2(photo2);
               existingDzongkhag.setPhoto2(imageUrl2);
           }
           if (photo3 != null && !photo3.isEmpty()) {
               String imageUrl3 = imageUploadService.uploadImage3(photo3);
               existingDzongkhag.setPhoto3(imageUrl3);
           }
           if (photo4 != null && !photo4.isEmpty()) {
               String imageUrl4 = imageUploadService.uploadImage4(photo4);
               existingDzongkhag.setPhoto4(imageUrl4);
           }
           if (photo5 != null && !photo5.isEmpty()) {
               String imageUrl5 = imageUploadService.uploadImage5(photo5);
               existingDzongkhag.setPhoto5(imageUrl5);
           }

           // Save the updated package
           dzongkhagService.update(id, existingDzongkhag);

           return ResponseEntity.status(HttpStatus.OK).body(existingDzongkhag);

       } catch (Exception e) {
           // Log the exception and return a meaningful error message;
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(null);
       }
   }   

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteDestination(@PathVariable int id) {
       dzongkhagService.deleteById(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

}
