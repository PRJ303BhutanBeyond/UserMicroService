package bt.edu.gcit.usermicroservice.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import bt.edu.gcit.usermicroservice.entity.Booking;
import bt.edu.gcit.usermicroservice.service.BookingService;

@RestController
@RequestMapping("/api")
public class BookingRestController {

    private BookingService bookingService;
    public BookingRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public Booking save(@RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @GetMapping("/bookings/all")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/bookings/{id}")
    public Booking getBooking(@PathVariable int id) {
        return bookingService.findById(id);
    }

    @DeleteMapping("/bookings/delete/{id}")
    public void deleteBooking(@PathVariable int id) {
        bookingService.deleteBooking(id);
    }

    @PutMapping("/bookings/{id}/paid")
    public ResponseEntity<?> updatePaidStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean paid = requestBody.get("paid");
        bookingService.updatePaidStatus(id, paid);
        System.out.println("User enabled status updated successfully");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bookings/{id}/done")
    public ResponseEntity<?> updateDoneStatus(
            @PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean done = requestBody.get("done");
        bookingService.updateDoneStatus(id, done);
        System.out.println("Guide Finished task");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bookings/{id}/assign")
    public ResponseEntity<?> assignGuide(
            @PathVariable int id, @RequestBody Map<String, String> requestBody) {
        String guide = requestBody.get("guide");
        String date = requestBody.get("date");
        bookingService.assignGuide(id, guide, date);
        System.out.println("Assign guide successful");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bookings/find")
    public List<Booking> getBookingByEmail(@RequestBody Map<String, String> requestBody) {
        String guide = requestBody.get("guide");
        return bookingService.findByGEmail(guide);
    }

    @PostMapping("/bookings/findByEmail")
    public List<Booking> getBookingByUserEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        return bookingService.findByEmail(email);
    }
}
