package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import bt.edu.gcit.usermicroservice.entity.TopDestination;
import java.util.List;

public interface TopDestinationDAO {
    List<TopDestination> findByDzongkhagOrderByNameAsc(Dzongkhag dzongkhag);

    List<TopDestination> findAll();

    TopDestination findById(int theId);

    TopDestination save(TopDestination theDestination, String dzongkhag_name);

    void deleteById(int theId);

    TopDestination update(TopDestination destination);

    List<TopDestination> listTopDestinationByDzongkhag(String theDzongkhag);

}
