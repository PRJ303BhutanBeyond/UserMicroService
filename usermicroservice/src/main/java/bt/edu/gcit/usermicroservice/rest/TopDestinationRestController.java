package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.dao.DzongkhagDAO;
import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import bt.edu.gcit.usermicroservice.entity.TopDestination;
import bt.edu.gcit.usermicroservice.service.TopDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api/topDestinations")
public class TopDestinationRestController {
    @Autowired
    private TopDestinationService topDestinationService;
    private ImageUploadService imageUploadService;

    @Autowired
    private DzongkhagDAO dzongkhagDAO;

    public TopDestinationRestController(TopDestinationService thetopDestinationService,
            ImageUploadService imageUploadService) {
        topDestinationService = thetopDestinationService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/", consumes = "multipart/form-data")
    public TopDestination save(@RequestPart("name") @Valid @NotNull String name,
            @RequestPart("description") @Valid @NotNull String description,
            @RequestPart("photo") @Valid @NotNull MultipartFile photo,
            @RequestPart("dzongkhagName") String dzongkhagName) {
        try {
            // Create a new User object
            TopDestination destination = new TopDestination();
            Dzongkhag dzongkhag = dzongkhagDAO.findByNameDzongkhag(dzongkhagName);
            if (dzongkhag == null) {
                throw new IllegalArgumentException("Dzongkhag with name " + dzongkhagName + " does not exist");
            }
            System.out.println("Dzongkhag: " + dzongkhag.getdzongkhagName());
            destination.setName(name);
            destination.setDescription(description);
            destination.setDzongkhag(dzongkhag);
            TopDestination savedDestination = topDestinationService.save(destination, dzongkhagName);
            // Upload the user photo
            System.out.println("Uploadingphoto" + savedDestination.getId().intValue());

            if (photo != null && !photo.isEmpty()) {
                String imageUrl = imageUploadService.uploadEventImage(photo);
                savedDestination.setPhoto(imageUrl);
                topDestinationService.update(savedDestination);
            }

            return savedDestination;
        } catch (Exception e) {
            // Handle the exception
            throw new RuntimeException("Error while uploading photo",
                    e);
        }
    }

    // @PostMapping("/{dzongkhagName}")
    // public ResponseEntity<TopDestination> createState(@RequestBody TopDestination
    // destination, @PathVariable String dzongkhagName) {
    // topDestinationService.save(destination, dzongkhagName);
    // return new ResponseEntity<>(HttpStatus.CREATED);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<TopDestination> getState(@PathVariable int id) {
        TopDestination destination = topDestinationService.findById(id);
        return new ResponseEntity<>(destination, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TopDestination>> getAllDestinations() {
        List<TopDestination> destinations = topDestinationService.findAll();
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @GetMapping("/dzongkhag/{dzongkhagName}")
    public ResponseEntity<List<TopDestination>> getTopDestinationsByDzongkhag(@PathVariable String dzongkhagName) {
        List<TopDestination> destinations = topDestinationService.listTopDestinationByDzongkhag(dzongkhagName);
        return new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<TopDestination> updateDestination(@PathVariable int id,
            @RequestPart("name") @Valid @NotNull String name,
            @RequestPart("description") @Valid @NotNull String description,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "dzongkhagName", required = false) String dzongkhagName) {
        try {
            // Retrieve the existing destination
            TopDestination existingDestination = topDestinationService.findById(id);
            if (existingDestination == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update the fields
            existingDestination.setName(name);
            existingDestination.setDescription(description);

            if (dzongkhagName != null) {
                Dzongkhag dzongkhag = dzongkhagDAO.findByNameDzongkhag(dzongkhagName);
                if (dzongkhag == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                existingDestination.setDzongkhag(dzongkhag);
            }

            // Update the photo if provided
            if (photo != null && !photo.isEmpty()) {
                String imageUrl = imageUploadService.uploadEventImage(photo);
                existingDestination.setPhoto(imageUrl);
            }

            // Save the updated destination
            topDestinationService.update(existingDestination);

            return new ResponseEntity<>(existingDestination, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable int id) {
        topDestinationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
