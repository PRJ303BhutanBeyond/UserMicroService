package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.TourPackage;
import java.util.List;

public interface PackageDAO {

    TourPackage save(TourPackage TourPackage);

    TourPackage findByID(int id);

    void deletePackage(long id);

    TourPackage updatePackage(TourPackage TourPackage);

    List<TourPackage> getAllTourPackages();
}
