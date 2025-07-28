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
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto"
        ));
        return result.get("secure_url").toString();
    }

    public String extractPublicId(String url) {
        // Tìm phần sau "/upload/"
        String[] parts = url.split("/upload/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("URL không hợp lệ");
        }

        // Lấy phần sau upload, ví dụ: v1234567890/my_folder/my_image.jpg
        String afterUpload = parts[1];

        // Bỏ phần version nếu có (bắt đầu bằng 'v' + số)
        String[] segments = afterUpload.split("/");
        int startIndex = segments[0].matches("^v\\d+$") ? 1 : 0;

        // Lấy tất cả các phần còn lại để tạo public_id
        StringBuilder publicIdBuilder = new StringBuilder();
        for (int i = startIndex; i < segments.length; i++) {
            publicIdBuilder.append(segments[i]);
            if (i < segments.length - 1) {
                publicIdBuilder.append("/");
            }
        }

        // Bỏ phần đuôi mở rộng (.jpg, .png...)
        String publicIdWithExtension = publicIdBuilder.toString();
        int dotIndex = publicIdWithExtension.lastIndexOf('.');
        return dotIndex != -1 ? publicIdWithExtension.substring(0, dotIndex) : publicIdWithExtension;
    }

    public boolean deleteFile(String url) throws IOException {
        String publicId = extractPublicId(url);
        Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"));
    }

}
