package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Booking;
import java.util.List;

public interface BookingDAO {
    Booking save(Booking booking);

    Booking getBookingById(int id);
    
    List<Booking> getAllBookings();

    List<Booking> findByGEmail(String guide);

    List<Booking> findByEmail(String email);

    void deleteBooking(int id);

    void updatePaidStatus(int id, boolean paid);

    void assignGuide(int id, String guide, String date);

    void updateDoneStatus(int id, boolean done);

}