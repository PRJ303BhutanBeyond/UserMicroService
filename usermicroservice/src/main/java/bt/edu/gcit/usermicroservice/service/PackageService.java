package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.entity.TourPackage;
import java.util.List;

public interface PackageService {

    TourPackage save(TourPackage TourPackage);

    void uploadPhoto(int id, MultipartFile photo_1, MultipartFile photo_2, MultipartFile photo_3, MultipartFile photo_4, MultipartFile photo_5) throws IOException;

    TourPackage findByID(int id);

    void deleteById(int theId);

    List<TourPackage> getAllTourPackages();

    TourPackage updatePackage(TourPackage tourPackage);

    TourPackage updatePackage(int id, TourPackage updatPackage);

} 
