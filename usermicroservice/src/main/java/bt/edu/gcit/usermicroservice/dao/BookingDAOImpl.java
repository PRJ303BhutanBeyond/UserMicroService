package bt.edu.gcit.usermicroservice.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import bt.edu.gcit.usermicroservice.entity.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import bt.edu.gcit.usermicroservice.exception.NotFoundException;

@Repository
public class BookingDAOImpl implements BookingDAO{
    private EntityManager entityManager;

    public BookingDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Booking save(Booking booking) {
        entityManager.persist(booking);
        return booking;
    }

    @Override
    public Booking getBookingById(int id) {
        return entityManager.find(Booking.class, id);
    }

    @Override
    public List<Booking> getAllBookings() {
        TypedQuery<Booking> query = entityManager.createQuery(
                "SELECT b FROM Booking b", Booking.class);
        return query.getResultList();
    }

    @Override
    public void deleteBooking(int id) {
        Booking booking = entityManager.find(Booking.class, id);
        if (booking != null) {
            entityManager.remove(booking);
        }
    }

    @Override
    public void updatePaidStatus(int id, boolean paid) {
        Booking booking = entityManager.find(Booking.class, id);
        System.out.println(booking);
        if (booking == null) {
            throw new NotFoundException("Booking form not found with id " + id);
        }
        booking.setPaid(paid);
        entityManager.persist(booking);
    }

    @Override
    public void updateDoneStatus(int id, boolean done) {
        Booking booking = entityManager.find(Booking.class, id);
        System.out.println(booking);
        if (booking == null) {
            throw new NotFoundException("Booking form not found with id " + id);
        }
        booking.setDone(done);
        entityManager.persist(booking);
    }

    @Override
    public void assignGuide(int id, String guide, String date) {
        Booking booking = entityManager.find(Booking.class, id);
        System.out.println(booking);
        if (booking == null) {
            throw new NotFoundException("Booking form not found with id " + id);
        }
        booking.setGuide(guide);
        booking.setDate(date);
        booking.setAssign(true);
        entityManager.persist(booking);
    }

    @Override
    public List<Booking> findByGEmail(String guide) {
        TypedQuery<Booking> query = entityManager.createQuery("FROM Booking WHERE guide = :guide", Booking.class);
        query.setParameter("guide", guide);
        return query.getResultList();
    }    

    @Override
    public List<Booking> findByEmail(String email) {
        TypedQuery<Booking> query = entityManager.createQuery("FROM Booking WHERE email = :email", Booking.class);
        query.setParameter("email", email);
        return query.getResultList();
    } 

    
}
