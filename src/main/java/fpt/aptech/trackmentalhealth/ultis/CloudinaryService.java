package fpt.aptech.trackmentalhealth.ultis;

import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private Cloudinary cloudinary;
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    @Value("${cloudinary.api_key}")
    private String apiKey;
    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
//    public CloudinaryService() {
//        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", cloudName,
//                "api_key", apiKey,
//                "api_secret", apiSecret
//        ));
//    }

    public String uploadFile(MultipartFile file) throws IOException {
//        Map uploadParams = ObjectUtils.asMap(
//                "public_id", "test_user_123"  // 👈 Đây là tên file bạn muốn
//        );
//
//        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return result.get("secure_url").toString();
    }
}
