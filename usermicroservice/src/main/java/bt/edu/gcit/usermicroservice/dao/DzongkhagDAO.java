package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import java.util.List;

public interface DzongkhagDAO {
    // void addDzongkhag(Dzongkhag dzongkhag);
    Dzongkhag save(Dzongkhag dzongkhag);

    Dzongkhag findByNameDzongkhag(String dzongkhagName);

    List<Dzongkhag> findAll();

   List<Dzongkhag> findAllByOrderByNameAsc();

    // Update
    Dzongkhag update(Dzongkhag dzongkhag);


    // Delete
    void deleteById(int theId);

    Dzongkhag findByID(int theId);

}
