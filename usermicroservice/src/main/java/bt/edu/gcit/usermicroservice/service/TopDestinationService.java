package bt.edu.gcit.usermicroservice.service;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import bt.edu.gcit.usermicroservice.entity.TopDestination;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface TopDestinationService {
    List<TopDestination> findByDzongkhagOrderByNameAsc(Dzongkhag dzongkhag);

    List<TopDestination> findAll();

    TopDestination findById(int theId);

    TopDestination save(TopDestination theDestination, String dzongkhag_name);

    void deleteById(int theId);

    TopDestination update(TopDestination destination);

    void uploadDestinationPhoto(int id, MultipartFile photo, String dzongkhag_name) throws IOException;

    List<TopDestination> listTopDestinationByDzongkhag(String theDzongkhag);
}

