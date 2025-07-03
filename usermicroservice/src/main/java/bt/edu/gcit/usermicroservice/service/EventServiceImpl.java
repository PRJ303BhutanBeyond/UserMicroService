package bt.edu.gcit.usermicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import bt.edu.gcit.usermicroservice.exception.NotFoundException;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.dao.EventDAO;
import bt.edu.gcit.usermicroservice.entity.Events;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.List;
import java.nio.file.Path;

@Service
public class EventServiceImpl implements EventService {
    private EventDAO eventDAO;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    public EventServiceImpl(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public Events findByID(int theId) {
        return eventDAO.findByID(theId);
    }

    @Override
    @Transactional
    public Events save(Events event) {
        return eventDAO.save(event);
    }

    @Override
    @Transactional
    public List<Events> findAll() {
        return eventDAO.findAll();
    }

    @Override
    @Transactional
    public Events update(int id, Events event) {
        Events existingEvents = eventDAO.findByID(id);

        existingEvents.setName(event.getName());
        existingEvents.setLocation(event.getLocation());
        existingEvents.setStartingDate(event.getLocation());
        existingEvents.setEndingDate(event.getEndingDate());
        existingEvents.setDescription(event.getDescription());


        return eventDAO.save(existingEvents);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        eventDAO.deleteById(theId);
    }

    @Transactional
    @Override
    public void uploadEventPhoto(int id, MultipartFile photo) throws IOException {
        Events event = findByID(id);
        if (event == null) {
            throw new NotFoundException("Recent event not found with id " + id);
        }
        if (photo.getSize() > 5 * 1024 * 1024) { 
            throw new FileSizeException("File size must be < 5MB");
        }

        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0,
                originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Append the timestamp to the filename
        String filename = filenameWithoutExtension + "_" + timestamp + "." +
                filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        event.setPhoto(filename);
        eventDAO.save(event);
    }
}
