package bt.edu.gcit.usermicroservice.rest;

import bt.edu.gcit.usermicroservice.entity.Events;
import bt.edu.gcit.usermicroservice.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import bt.edu.gcit.usermicroservice.service.ImageUploadService;

@RestController
@RequestMapping("/api/events")
public class EventRestController {
    private EventService eventService;
    private ImageUploadService imageUploadService;

    public EventRestController(EventService theEventService, ImageUploadService imageUploadService) {
        eventService = theEventService;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public Events save(@RequestPart("name") @Valid @NotNull String name,
            @RequestPart("location") @Valid @NotNull String location,
            @RequestPart("startingDate") @Valid @NotNull String startingDate,
            @RequestPart("endingDate") @Valid @NotNull String endingDate,
            @RequestPart("description") @Valid @NotNull String description,
            @RequestPart("photo") @Valid @NotNull MultipartFile photo) throws IOException {
        // Create a new Event object
        Events event = new Events();
        event.setName(name);
        event.setLocation(location);
        event.setStartingDate(startingDate);
        event.setEndingDate(endingDate);
        event.setDescription(description);

        Events savedEvent = eventService.save(event);
        System.out.println("Uploadingphoto" + savedEvent.getId().intValue());
        
        String imageUrl1 = imageUploadService.uploadEventImage(photo);
        savedEvent.setPhoto(imageUrl1);
        
        eventService.update(savedEvent.getId().intValue(), savedEvent);

        return savedEvent;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Events>> getAllEvents() {
        List<Events> events = eventService.findAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Events> updateEvent(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("startingDate") String startingDate,
            @RequestParam("endingDate") String endingDate,
            @RequestParam("description") String description,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        try {
            Events existingEvent = eventService.findByID(id);

            if (existingEvent == null) {
                return ResponseEntity.notFound().build();
            }

            existingEvent.setName(name);
            existingEvent.setLocation(location);
            existingEvent.setStartingDate(startingDate);
            existingEvent.setEndingDate(endingDate);
            existingEvent.setDescription(description);

            if (photo != null && !photo.isEmpty()) {
                String imageUrl = imageUploadService.uploadEventImage(photo);
                existingEvent.setPhoto(imageUrl);
            }

            eventService.update(id, existingEvent);

            return ResponseEntity.ok(existingEvent);

        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable int id) {
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public Events findByID(@PathVariable int id) {
        return eventService.findByID(id);
    }

}
