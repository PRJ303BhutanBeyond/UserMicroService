package bt.edu.gcit.usermicroservice.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import bt.edu.gcit.usermicroservice.dao.PackageDAO;
import bt.edu.gcit.usermicroservice.entity.TourPackage;

import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageDAO packageDAO;
    private final String uploadDir = "src/main/resources/static/images";

    @Autowired
    @Lazy
    public PackageServiceImpl(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    @Override
    @Transactional
    public TourPackage save(TourPackage TourPackage) {
        return packageDAO.save(TourPackage);
    }

    @Override
    public TourPackage findByID(int id) {
        return packageDAO.findByID(id);
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        packageDAO.deletePackage(theId);
    }

    @Override
    @Transactional
    public List<TourPackage> getAllTourPackages() {
        return packageDAO.getAllTourPackages();
    }

    @Override
    @Transactional
    public TourPackage updatePackage(TourPackage tourPackage) {
        return packageDAO.updatePackage(tourPackage);
    }

    @Override
    @Transactional
    public TourPackage updatePackage(int id, TourPackage updatPackage) {
        TourPackage existingPackage = packageDAO.findByID(id);

        if (existingPackage == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        existingPackage.setTopic(updatPackage.getTopic());
        existingPackage.setType(updatPackage.getType());
        existingPackage.setOverview(updatPackage.getOverview());
        existingPackage.setPrice(updatPackage.getPrice());
        existingPackage.setLocation(updatPackage.getLocation());
        existingPackage.setDurationString(updatPackage.getDurationString());
        existingPackage.setPreferredMonth(updatPackage.getPreferredMonth());
        existingPackage.setHighlight(updatPackage.getHighlight());
        existingPackage.setDailyItinerary(updatPackage.getDailyItinerary());
        existingPackage.setIncluded(updatPackage.getIncluded());
        existingPackage.setExcluded(updatPackage.getExcluded());

        return packageDAO.save(existingPackage);
    }

    @Transactional
    @Override
    public void uploadPhoto(int id, MultipartFile photo_1, MultipartFile photo_2, MultipartFile photo_3,
            MultipartFile photo_4, MultipartFile photo_5) throws IOException {
        TourPackage tourPackage = findByID(id);

        if (tourPackage == null) {
            throw new UserNotFoundException("Package not found with id " + id);
        }

        if (photo_1 != null && !photo_1.isEmpty()) {
            uploadPhoto1(photo_1, tourPackage);
        }

        if (photo_2 != null && !photo_2.isEmpty()) {
            uploadPhoto2(photo_2, tourPackage);
        }

        if (photo_3 != null && !photo_3.isEmpty()) {
            uploadPhoto3(photo_3, tourPackage);
        }

        if (photo_4 != null && !photo_4.isEmpty()) {
            uploadPhoto4(photo_4, tourPackage);
        }

        if (photo_5 != null && !photo_5.isEmpty()) {
            uploadPhoto5(photo_5, tourPackage);
        }
    }

    private void uploadPhoto1(MultipartFile photo1, TourPackage tourPackage) throws IOException {
        if (photo1.getSize() > 1024 * 1024) {
            throw new FileSizeException("Profile photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo1.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo1.transferTo(uploadPath);
        tourPackage.setPhoto1(filename);
    }

    private void uploadPhoto2(MultipartFile photo2, TourPackage tourPackage) throws IOException {
        if (photo2.getSize() > 1024 * 1024) {
            throw new FileSizeException("Profile photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo2.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo2.transferTo(uploadPath);
        tourPackage.setPhoto2(filename);
    }

    private void uploadPhoto3(MultipartFile photo3, TourPackage tourPackage) throws IOException {
        if (photo3.getSize() > 1024 * 1024) {
            throw new FileSizeException("Profile photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo3.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo3.transferTo(uploadPath);
        tourPackage.setPhoto3(filename);
    }

    private void uploadPhoto4(MultipartFile photo4, TourPackage tourPackage) throws IOException {
        if (photo4.getSize() > 1024 * 1024) {
            throw new FileSizeException("Profile photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo4.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo4.transferTo(uploadPath);
        tourPackage.setPhoto4(filename);
    }

    private void uploadPhoto5(MultipartFile photo5, TourPackage tourPackage) throws IOException {
        if (photo5.getSize() > 1024 * 1024) {
            throw new FileSizeException("Profile photo size must be < 1MB");
        }
        String originalFilename = StringUtils.cleanPath(photo5.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo5.transferTo(uploadPath);
        tourPackage.setPhoto5(filename);
    }

}
