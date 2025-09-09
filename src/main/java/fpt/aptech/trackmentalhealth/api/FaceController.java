package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.UserFaceEmbedding;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.repository.login.UserFaceEmbeddingRepository;
import fpt.aptech.trackmentalhealth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FaceController {
    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final LoginRepository loginRepository;
    private final UserFaceEmbeddingRepository userFaceEmbeddingRepository;

    // Đăng ký kèm faceId
    @PostMapping("/register-with-face")
    public ResponseEntity<?> registerWithFace(
            @RequestParam Integer userId,
            @RequestPart("faceImage") MultipartFile faceImage
    ) throws IOException {
        // Kiểm tra user tồn tại chưa
        Optional<Users> userOpt = loginRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        Users user = userOpt.get();

        // Gửi ảnh sang Flask
        String flaskUrl = "http://localhost:5000/generate-embedding";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(faceImage.getBytes()) {
            @Override
            public String getFilename() {
                return faceImage.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, requestEntity, String.class);

        // Lưu embedding vào DB
        UserFaceEmbedding embedding = new UserFaceEmbedding();
        embedding.setUser(user);
        embedding.setEmbedding(response.getBody()); // lưu JSON string
        userFaceEmbeddingRepository.save(embedding);

        return ResponseEntity.ok(Map.of(
                "message", "Face registered successfully",
                "userId", user.getId()
        ));
    }


    // Login bằng faceId (Flutter gửi userId sau khi Flask verify xong)
    @PostMapping("/login-face")
    public ResponseEntity<?> loginByFace(@RequestParam String username) {
        Map<String, String> result = userService.loginUsersByFaceId(username);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }
}
