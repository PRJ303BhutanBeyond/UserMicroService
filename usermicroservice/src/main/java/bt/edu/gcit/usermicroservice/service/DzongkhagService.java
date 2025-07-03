package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import bt.edu.gcit.usermicroservice.entity.Dzongkhag;

public interface DzongkhagService {
    // void addDzongkhag(Dzongkhag dzongkhag);
    Dzongkhag save(Dzongkhag dzongkhag);

    Dzongkhag findByNameDzongkhag(String dzongkhagName);

    List<Dzongkhag> findAll();

    List<Dzongkhag> findAllByOrderByNameAsc();

    // Update

    Dzongkhag update(Dzongkhag dzongkhag);

    Dzongkhag update(int id, Dzongkhag dzongkhag);

    // Delete
    void deleteById(int theId);

    void uploadPhoto(int id, MultipartFile photo1, MultipartFile photo2, MultipartFile photo3, MultipartFile photo4, MultipartFile photo5 ) throws IOException;

    Dzongkhag findByID(int theId);

}
