package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.RegisterUserRequestDTO;
import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.EditProfileDTO;
import fpt.aptech.trackmentalhealth.entities.PendingUserRegistration;
import fpt.aptech.trackmentalhealth.entities.Role;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.repository.login.PendingUserRepository;
import fpt.aptech.trackmentalhealth.service.user.EmailService;
import fpt.aptech.trackmentalhealth.service.user.UserService;
import fpt.aptech.trackmentalhealth.ultis.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PendingUserRepository pendingUserRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    private MultipartFile avatar;

    // === REGISTER ===
    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(@ModelAttribute RegisterUserRequestDTO request) {
        try {
            Integer roleId = request.getRoleId();
            // Kiểm tra role ID hợp lệ
            if (roleId == null || roleId < 1 || roleId > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid role ID."));
            }

            // Các role cần upload chứng chỉ: 2, 4, 5
            boolean requiresCertificate = roleId == 2 || roleId == 4 || roleId == 5;

            if (requiresCertificate) {
                MultipartFile[] files = request.getCertificates();
                if (files == null || files.length < 1 || files.length > 5) {
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "You must upload between 1 and 5 certificates for this role.")
                    );
                }
            }

            // Kiểm tra email trùng
            if (loginRepository.findByEmail(request.getEmail()) != null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is already registered."));
            }

            // Tạo bản ghi chờ duyệt
            PendingUserRegistration pending = new PendingUserRegistration();
            pending.setEmail(request.getEmail());
            pending.setPassword(passwordEncoder.encode(request.getPassword()));
            pending.setFullName(request.getFullName());
            pending.setRoleId(roleId);
            pending.setSubmittedAt(LocalDateTime.now());

            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                String avatarUrl = cloudinaryService.uploadFile(request.getAvatar());
                pending.setAvatar(avatarUrl);
            }

            // Lưu chứng chỉ nếu có
            if (requiresCertificate) {
                MultipartFile[] files = request.getCertificates();
                if (files.length >= 1) pending.setCertificate1(files[0].getBytes());
                if (files.length >= 2) pending.setCertificate2(files[1].getBytes());
                if (files.length >= 3) pending.setCertificate3(files[2].getBytes());
                if (files.length >= 4) pending.setCertificate4(files[3].getBytes());
                if (files.length == 5) pending.setCertificate5(files[4].getBytes());
            }

            pendingUserRepository.save(pending);
            return ResponseEntity.ok(Map.of("message", "Registration submitted. Awaiting admin approval."));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Registration failed",
                    "details", e.getMessage()
            ));
        }
    }


    // == Approve User ===
    @PostMapping("/approve/{pendingId}")
    public ResponseEntity<?> approveUser(@PathVariable Integer pendingId) {
        Optional<PendingUserRegistration> optional = pendingUserRepository.findById(pendingId);
        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Pending user not found"));
        }

        PendingUserRegistration pending = optional.get();
        if (pending.getIsReviewed()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User already reviewed"));
        }

        Users user = new Users();
        user.setEmail(pending.getEmail());
        user.setPassword(pending.getPassword());
        user.setUsername(pending.getFullName());
        user.setFullname(pending.getFullName());
        user.setIsApproved(true);

        Role role = new Role();
        role.setId(pending.getRoleId());
        user.setRoleId(role);

        loginRepository.save(user);

        pending.setIsReviewed(true);
        pending.setIsApproved(true);
        pendingUserRepository.save(pending);

        return ResponseEntity.ok(Map.of("message", "User approved and account created"));
    }

    // === Get Pending Register ===
    @GetMapping("/pending-registrations")
    public ResponseEntity<?> listPendingUsers() {
        return ResponseEntity.ok(pendingUserRepository.findByIsReviewedFalse());
    }


    // === LOGIN ===
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        String token = userService.loginUsers(userDTO);
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
    @GetMapping("/by-role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(@PathVariable Integer roleId) {
        List<Users> users = loginRepository.findByRoleId_Id(roleId);
        return ResponseEntity.ok(users.stream().map(user -> Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullname", user.getFullname(),
                "avatar", user.getAvatar()
        )));
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getProfile(@PathVariable Integer id) {
        Optional<Users> optionalUser = loginRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Users user = optionalUser.get();

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullname", user.getFullname(),
                "username", user.getUsername(),
                "avatar", user.getAvatar(),
                "address", user.getAddress(),
                "dob", user.getDob(),
                "gender", user.getGender(),
                "role", user.getRoleId() != null ? user.getRoleId().getRoleName() : null
        ));
    }

    @PostMapping(value = "/edit-profile", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'PSYCHOLOGIST', 'TEST_DESIGNER', 'CONTENT_CREATOR')")
    public ResponseEntity<?> editProfile(
            @ModelAttribute EditProfileDTO request,
            Authentication authentication
    ) {
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            Optional<Users> optionalUser = userService.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            Users user = optionalUser.get();

            // Cập nhật thông tin
            user.setFullname(request.getFullname());
            user.setAddress(request.getAddress());
            user.setDob(request.getDob());
            user.setGender(request.getGender());

            // Nếu có file avatar mới
            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                String avatarUrl = cloudinaryService.uploadFile(request.getAvatar());
                user.setAvatar(avatarUrl);
            }

            loginRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Update failed", "details", e.getMessage()));
        }
    }

    // === FORGOT PASSWORD ===
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Users users = loginRepository.findByEmail(email);
        if (users == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid email"));
        }

        String otp = generateOTP();
        users.setOtp(otp);
        users.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        loginRepository.save(users);
        emailService.sendOtpEmail(users.getEmail(), otp);
        return ResponseEntity.ok(Map.of("message", "OTP has been sent to your email"));
    }

    // === VERIFY OTP ===
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Users users = loginRepository.findByEmail(email);
        if (users == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid email"));
        }
        if (users.getOtp() == null || !users.getOtp().trim().equals(otp.trim())) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid OTP"));
        }
        if (users.getOtpExpiry() == null || users.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("error", "OTP has expired"));
        }

        return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
    }

    // === RESET PASSWORD ===
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        Users users = loginRepository.findByEmail(email.trim());
        if (users == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid email"));
        }

        users.setPassword(passwordEncoder.encode(newPassword));
        users.setOtp(null);
        users.setOtpExpiry(null);
        loginRepository.save(users);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

    // === OTP GENERATOR ===
    private String generateOTP() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000)); // 6 digits
    }


}