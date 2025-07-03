package bt.edu.gcit.usermicroservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
    private String upload(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not upload image", e);
        }
    }

    public String uploadImage(MultipartFile file) {
        return upload(file);
    }

    public String uploadEventImage(MultipartFile file) {
        return upload(file);
    }

    public String uploadLicenseImage(MultipartFile file) {
        return upload(file);
    }

    public String uploadImage1(MultipartFile file) {
        return upload(file);
    }

    public String uploadImage2(MultipartFile file) {
        return upload(file);
    }

    public String uploadImage3(MultipartFile file) {
        return upload(file);
    }

    public String uploadImage4(MultipartFile file) {
        return upload(file);
    }

    public String uploadImage5(MultipartFile file) {
        return upload(file);
    }
}
