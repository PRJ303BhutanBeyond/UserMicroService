package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import bt.edu.gcit.usermicroservice.dao.DzongkhagDAO;
import bt.edu.gcit.usermicroservice.dao.TopDestinationDAO;
import bt.edu.gcit.usermicroservice.entity.TopDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.util.StringUtils;
import java.nio.file.Path;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import java.nio.file.Paths;
import bt.edu.gcit.usermicroservice.exception.NotFoundException;

@Service
public class TopDestinationServiceImpl implements TopDestinationService {
    private TopDestinationDAO topDestinationDAO;
    private DzongkhagDAO dzongkhagDAO;
    private final String uploadDir = "src/main/resources/static/images";

    public TopDestinationServiceImpl(TopDestinationDAO theTopDestinationDAO, DzongkhagDAO theDzongkhagDAO) {
        topDestinationDAO = theTopDestinationDAO;
        dzongkhagDAO = theDzongkhagDAO;
    }

    @Override
    @Transactional
    public List<TopDestination> findByDzongkhagOrderByNameAsc(Dzongkhag dzongkhag) {
        return topDestinationDAO.findByDzongkhagOrderByNameAsc(dzongkhag);
    }

    @Override
    @Transactional
    public TopDestination update(TopDestination destination) {
        return topDestinationDAO.update(destination);
    }

    @Override
    @Transactional
    public List<TopDestination> findAll() {
        return topDestinationDAO.findAll();
    }

    @Override
    @Transactional
    public TopDestination findById(int theId) {
        return topDestinationDAO.findById(theId);
    }

    @Override
    @Transactional
    public TopDestination save(TopDestination theDestination, String dzongkhag_name) {
        Dzongkhag dzongkhag = dzongkhagDAO.findByNameDzongkhag(dzongkhag_name);
        if (dzongkhag == null) {
            throw new IllegalArgumentException("Dzongkhag with name " + dzongkhag_name + " does not exist");
        }
        System.out.println("Dzongkhag: " + dzongkhag.getdzongkhagName());
        TopDestination destination = new TopDestination();
        destination.setName(theDestination.getName());
        destination.setDescription(theDestination.getDescription());
        destination.setPhoto(theDestination.getPhoto());
        destination.setDzongkhag(dzongkhag);
        System.out.println("Top Destination: " + destination.getDzongkhag());
        return topDestinationDAO.save(destination, dzongkhag_name);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        topDestinationDAO.deleteById(theId);
    }

    @Override
    @Transactional
    public List<TopDestination> listTopDestinationByDzongkhag(String theDzongkhag) {
        return topDestinationDAO.listTopDestinationByDzongkhag(theDzongkhag);
    }

    @Transactional
    @Override
    public void uploadDestinationPhoto(int id, MultipartFile photo, String dzongkhag_name) throws IOException {
        TopDestination destination = findById(id);
        Dzongkhag dzongkhag = dzongkhagDAO.findByNameDzongkhag(dzongkhag_name);
        if (dzongkhag == null) {
            throw new IllegalArgumentException("Dzongkhag with name " + dzongkhag_name + " does not exist");
        }
        if (destination == null) {
            throw new NotFoundException("Destination not found with id " + id);
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
        destination.setPhoto(filename);
        topDestinationDAO.save(destination, dzongkhag_name);
    }

}
