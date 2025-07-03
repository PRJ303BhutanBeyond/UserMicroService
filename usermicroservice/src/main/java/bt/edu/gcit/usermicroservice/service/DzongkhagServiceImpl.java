package bt.edu.gcit.usermicroservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import bt.edu.gcit.usermicroservice.exception.NotFoundException;
import bt.edu.gcit.usermicroservice.exception.FileSizeException;
import bt.edu.gcit.usermicroservice.dao.DzongkhagDAO;
import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Path;

@Service
public class DzongkhagServiceImpl implements DzongkhagService {
    private DzongkhagDAO dzongkhagDAO;
    private final String uploadDir = "src/main/resources/static/images";

    public DzongkhagServiceImpl(DzongkhagDAO dzongkhagDAO) {
        this.dzongkhagDAO = dzongkhagDAO;
    }

    @Override
    public Dzongkhag findByID(int theId) {
        return dzongkhagDAO.findByID(theId);
    }

    @Override
    @Transactional
    public Dzongkhag save(Dzongkhag dzongkhag) {
        return dzongkhagDAO.save(dzongkhag);
    }

    @Override
    @Transactional
    public Dzongkhag findByNameDzongkhag(String dzongkhagName) {
        return dzongkhagDAO.findByNameDzongkhag(dzongkhagName);
    }

    @Override
    @Transactional
    public List<Dzongkhag> findAll() {
        return dzongkhagDAO.findAll();
    }

    @Override
    @Transactional
    public List<Dzongkhag> findAllByOrderByNameAsc() {
        return dzongkhagDAO.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional
    public Dzongkhag update(Dzongkhag dzongkhag) {
        return dzongkhagDAO.update(dzongkhag);
    }

    @Override
    @Transactional
    public Dzongkhag update( int id, Dzongkhag dzongkhag) {
        Dzongkhag existingDzongkhag = dzongkhagDAO.findByID(id);

        existingDzongkhag.setdzongkhagName(dzongkhag.getdzongkhagName());
        existingDzongkhag.setSubtitle(dzongkhag.getSubtitle());
        existingDzongkhag.setDescription(dzongkhag.getDescription());
        existingDzongkhag.setRoute(dzongkhag.getRoute());
        existingDzongkhag.setTemperature(dzongkhag.getTemperature());

        return dzongkhagDAO.save(existingDzongkhag);
    }
    
    @Override 
    @Transactional
    public void deleteById(int theId) {
        dzongkhagDAO.deleteById(theId);
    }

    @Transactional
    @Override
    public void uploadPhoto(int id, MultipartFile photo1, MultipartFile photo2, MultipartFile photo3, MultipartFile photo4, MultipartFile photo5) throws IOException {
        Dzongkhag dzongkhag = findByID(id);

        if (dzongkhag == null) {
            throw new NotFoundException("Dzongkhag not found with id " + id);
        }

        if (photo1 != null && !photo1.isEmpty()) {
            uploadDzongkhagPhoto1(photo1, dzongkhag);
        }

        if (photo2 != null && !photo2.isEmpty()) {
            uploadDzongkhagPhoto2(photo2, dzongkhag);
        }

        if (photo3 != null && !photo3.isEmpty()) {
            uploadDzongkhagPhoto3(photo3, dzongkhag);
        }

        if (photo4 != null && !photo4.isEmpty()) {
            uploadDzongkhagPhoto4(photo4, dzongkhag);
        }

        if (photo5 != null && !photo5.isEmpty()) {
            uploadDzongkhagPhoto5(photo5, dzongkhag);
        }
    }

    private void uploadDzongkhagPhoto1(MultipartFile photo, Dzongkhag dzongkhag) throws IOException {
        if (photo.getSize() > 5 * 1024 * 1024) { 
            throw new FileSizeException("File size must be < 5MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        dzongkhag.setPhoto1(filename);
    }

    private void uploadDzongkhagPhoto2(MultipartFile photo, Dzongkhag dzongkhag) throws IOException {
        if (photo.getSize() > 5 * 1024 * 1024) { 
            throw new FileSizeException("File size must be < 5MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        dzongkhag.setPhoto2(filename);
    }

    private void uploadDzongkhagPhoto3(MultipartFile photo, Dzongkhag dzongkhag) throws IOException {
        if (photo.getSize() > 5 * 1024 * 1024) { 
            throw new FileSizeException("File size must be < 5MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        dzongkhag.setPhoto3(filename);
    }

    private void uploadDzongkhagPhoto4(MultipartFile photo, Dzongkhag dzongkhag) throws IOException {
        if (photo.getSize() > 5 * 1024 * 1024) { 
            throw new FileSizeException("File size must be < 5MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        dzongkhag.setPhoto4(filename);
    }

    private void uploadDzongkhagPhoto5(MultipartFile photo, Dzongkhag dzongkhag) throws IOException {
        if (photo.getSize() > 5 * 1024 * 1024) { 
            throw new FileSizeException("File size must be < 5MB");
        }
        String originalFilename = StringUtils.cleanPath(photo.getOriginalFilename());
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String filenameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = filenameWithoutExtension + "_" + timestamp + "." + filenameExtension;
        Path uploadPath = Paths.get(uploadDir, filename);
        photo.transferTo(uploadPath);
        dzongkhag.setPhoto5(filename);
    }
}
