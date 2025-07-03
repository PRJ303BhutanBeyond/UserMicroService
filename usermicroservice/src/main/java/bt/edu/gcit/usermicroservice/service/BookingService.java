package bt.edu.gcit.usermicroservice.service;

import java.util.List;
import bt.edu.gcit.usermicroservice.entity.Booking;

public interface BookingService {
    Booking save(Booking booking);

    Booking findById(int id);
    
    List<Booking> getAllBookings();

    void deleteBooking(int id);

    void updatePaidStatus(int id, boolean paid);

    void assignGuide(int id, String guide, String date);

    List<Booking> findByGEmail(String guide);

    List<Booking> findByEmail(String email);

    void updateDoneStatus(int id, boolean done);
}
