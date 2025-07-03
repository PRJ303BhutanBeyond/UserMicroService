package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import bt.edu.gcit.usermicroservice.entity.Events;

public interface EventService {

     Events save(Events event);

    List<Events> findAll();

    // Update
    Events update(int id, Events event);

    // Delete
    void deleteById(int theId);

    Events findByID(int theId);

    void uploadEventPhoto(int id, MultipartFile photo) throws IOException;
    
}
