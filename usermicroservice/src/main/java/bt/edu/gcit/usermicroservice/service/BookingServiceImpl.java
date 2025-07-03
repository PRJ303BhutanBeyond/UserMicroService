package bt.edu.gcit.usermicroservice.service;

import java.util.List;
import org.springframework.context.annotation.Lazy;
import bt.edu.gcit.usermicroservice.entity.Booking;
import bt.edu.gcit.usermicroservice.dao.BookingDAO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingServiceImpl implements BookingService{
    private final BookingDAO bookingDAO;

    @Lazy
    public BookingServiceImpl(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Override
    @Transactional
    public Booking save(Booking booking) {
        return bookingDAO.save(booking);
    }

    @Override
    public Booking findById(int id) {
        return bookingDAO.getBookingById(id);
    }

    @Override
    @Transactional
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    @Override
    @Transactional
    public void deleteBooking(int id) {
        bookingDAO.deleteBooking(id);
    }

    @Override
    @Transactional
    public void updatePaidStatus(int id, boolean paid) {
        bookingDAO.updatePaidStatus(id, paid);
    }

    @Override
    @Transactional
    public void assignGuide(int id, String guide, String date) {
        bookingDAO.assignGuide(id, guide, date);
    }

    @Override
    @Transactional
    public List<Booking> findByGEmail(String guide) {
        return bookingDAO.findByGEmail(guide);
    }

    @Override
    @Transactional
    public List<Booking> findByEmail(String email) {
        return bookingDAO.findByEmail(email);
    }

    @Override
    @Transactional
    public void updateDoneStatus(int id, boolean done) {
        bookingDAO.updateDoneStatus(id, done);
    }
}
