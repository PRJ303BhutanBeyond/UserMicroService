package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import bt.edu.gcit.usermicroservice.entity.Events;

public interface EventDAO {
    
    Events save(Events event);

    List<Events> findAll();

    // Update
    void update(Events event);

    // Delete
    void deleteById(int theId);

    Events findByID(int theId);
}
