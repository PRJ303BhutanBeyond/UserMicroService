package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Events;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import java.util.List;

@Repository
public class EventDAOImpl implements EventDAO {
    private EntityManager entityManager;

    public EventDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Events findByID(int theId) {
        return entityManager.find(Events.class, theId);
    }

    @Override
    public Events save(Events dzongkhag) {
        return entityManager.merge(dzongkhag);
    }

    @Override
    public List<Events> findAll() {
        return entityManager.createQuery("SELECT e FROM Events e", Events.class)
                .getResultList();
    }

    @Override
    public void update(Events event) {
        entityManager.merge(event);
    }

    @Override
    public void deleteById(int theId) {
        Events event = entityManager.find(Events.class, theId);
        entityManager.remove(event);
    }

}
