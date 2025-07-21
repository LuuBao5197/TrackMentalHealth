package fpt.aptech.trackmentalhealth.api;
import fpt.aptech.trackmentalhealth.entities.Role;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.ultis.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/auth/oauth")
public class OAuthController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private final RestTemplate restTemplate = new RestTemplate();

    // ========================= GOOGLE =========================
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestParam String idToken) {
        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            Map<String, Object> payload = restTemplate.getForObject(url, Map.class);

            if (payload == null || !payload.containsKey("email")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
            }

            String email = (String) payload.get("email");
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            return handleOAuthLogin(email, name, picture);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google OAuth failed: " + e.getMessage());
        }
    }

    // ========================= FACEBOOK =========================
    @PostMapping("/facebook")
    public ResponseEntity<?> loginWithFacebook(@RequestParam String accessToken) {
        try {
            String fields = "id,name,email,picture";
            String url = "https://graph.facebook.com/me?fields=" + fields + "&access_token=" + accessToken;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("email")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Facebook token");
            }

            String email = (String) response.get("email");
            String name = (String) response.get("name");

            // Extract picture URL
            Map<String, Object> pictureData = (Map<String, Object>) ((Map<String, Object>) response.get("picture")).get("data");
            String pictureUrl = (String) pictureData.get("url");

            return handleOAuthLogin(email, name, pictureUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Facebook OAuth failed: " + e.getMessage());
        }
    }

    // ========================= COMMON HANDLER =========================
    private ResponseEntity<?> handleOAuthLogin(String email, String name, String avatarUrl) {
        Users user = loginRepository.findByEmail(email);

        if (user == null) {
            user = new Users();
            user.setEmail(email);
            user.setFullname(name);
            user.setUsername(email); // Hoặc custom
            user.setIsApproved(true);
            user.setAvatar(avatarUrl);

            // Set default role
            Role role = new Role();
            role.setId(1); // ROLE_USER
            user.setRoleId(role);

            loginRepository.save(user);
        }

        // Generate JWT
        String jwt = jwtUtils.generateToken(email, user, null);

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("userId", user.getId());
        result.put("role", user.getRoleId().getRoleName());
        result.put("fullname", user.getFullname());
        result.put("avatar", user.getAvatar());

        return ResponseEntity.ok(result);
    }
}
