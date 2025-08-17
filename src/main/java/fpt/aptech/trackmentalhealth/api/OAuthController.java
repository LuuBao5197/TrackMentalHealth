package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Role;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.repository.login.RoleRepository;
import fpt.aptech.trackmentalhealth.ultis.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/auth/oauth")
public class OAuthController {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

            Users existingUser = loginRepository.findByEmail(email);
            Users user;

            if (existingUser != null) {
                if (existingUser.getPassword() != null && !existingUser.getPassword().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Email already exists, please login with password");
                }
                // Login tiếp tục với OAuth user
                return handleOAuthLogin(existingUser.getEmail(), existingUser.getFullname(), existingUser.getAvatar());
            } else {
                // Tạo user mới
                Role userRole = roleRepository.findByRoleName("USER")
                        .orElseThrow(() -> new RuntimeException("Default role USER not found"));

                // Tạo password ngẫu nhiên
                String randomPassword = UUID.randomUUID().toString();

                // Băm password nếu bạn dùng Spring Security
                String encodedPassword = passwordEncoder.encode(randomPassword);

                user = new Users();
                user.setEmail(email);
                user.setFullname(name);
                user.setAvatar(picture);
                user.setRoleId(userRole);
                user.setPassword(encodedPassword);

                loginRepository.save(user);
            }

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
            user.setUsername(email);
            user.setIsApproved(true);
            user.setAvatar(avatarUrl);

            // Set default role
            Role role = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            user.setRoleId(role);

            loginRepository.save(user);
        }

        // Generate JWT
        String jwt = jwtUtils.generateToken(email, user, null);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("fullname", user.getFullname());
        userData.put("email", user.getEmail());
        userData.put("avatar", user.getAvatar());
        userData.put("role", user.getRoleId().getRoleName());

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("user", userData);

        return ResponseEntity.ok(result);
    }

}
