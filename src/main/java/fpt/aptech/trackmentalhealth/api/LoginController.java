package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repositories.LoginRepository;
import fpt.aptech.trackmentalhealth.services.EmailService;
import fpt.aptech.trackmentalhealth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserService userService;

    // === LOGIN ===
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        String token = userService.loginUsers(userDTO);
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    // === PROFILE ===
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername(); // Lấy tên người dùng đúng cách

        Optional<Users> userOpt = userService.findByEmail(email);
        return userOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // === FORGOT PASSWORD ===
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Users user = loginRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid email"));
        }

        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        loginRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(Map.of("message", "OTP has been sent to your email"));
    }

    // === VERIFY OTP ===
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Users user = loginRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid email"));
        }
        if (user.getOtp() == null || !user.getOtp().trim().equals(otp.trim())) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid OTP"));
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("error", "OTP has expired"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
    }

    // === RESET PASSWORD ===
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        Users user = loginRepository.findByEmail(email.trim());
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid email"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiry(null);
        loginRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    // === OTP GENERATOR ===
    private String generateOTP() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // 6 digits
    }
}
