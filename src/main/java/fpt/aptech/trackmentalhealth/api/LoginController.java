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
import java.util.*;

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

    // === GỬI OTP ĐĂNG KÝ ===
    private final Map<String, String> pendingOtps = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiryMap = new HashMap<>();

    @PostMapping("/send-otp-register")
    public ResponseEntity<?> sendOtpForRegister(@RequestParam String email) {
        if (loginRepository.findByEmail(email) != null) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        String otp = generateOTP();
        pendingOtps.put(email, otp);
        otpExpiryMap.put(email, LocalDateTime.now().plusMinutes(5));

        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to email"));
    }

    @PostMapping("/verify-otp-register")
    public ResponseEntity<?> verifyOtpForRegister(@RequestParam String email, @RequestParam String otp) {
        System.out.println("Verify OTP for email: " + email);
        System.out.println("Stored OTP: " + pendingOtps.get(email));
        System.out.println("Stored Expiry: " + otpExpiryMap.get(email));
        System.out.println("Now: " + LocalDateTime.now());

        if (!pendingOtps.containsKey(email)) {
            return ResponseEntity.status(400).body(Map.of("error", "OTP not found"));
        }

        if (!pendingOtps.get(email).equals(String.valueOf(otp))) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid OTP"));
        }

        if (otpExpiryMap.get(email).isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("error", "OTP has expired"));
        }

        pendingOtps.remove(email);
        otpExpiryMap.remove(email);

        return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
    }

    // === REGISTER ===
    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(@ModelAttribute RegisterUserRequestDTO request) {
        try {
            Integer roleId = request.getRoleId();
            // Kiểm tra role ID hợp lệ
            if (roleId == null || roleId < 1 || roleId > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid role ID."));
            }

            // Kiểm tra email trùng
            if (loginRepository.findByEmail(request.getEmail()) != null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is already registered."));
            }

            // Role cần xét duyệt
            boolean requiresApproval = roleId == 2 || roleId == 3 || roleId == 4;

            // Nếu cần duyệt thì lưu vào PendingUserRegistration
            if (requiresApproval) {
                MultipartFile[] files = request.getCertificates();
                if (files == null || files.length < 1 || files.length > 5) {
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "You must upload between 1 and 5 certificates for this role.")
                    );
                }

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
                if (files.length >= 1) pending.setCertificate1(files[0].getBytes());
                if (files.length >= 2) pending.setCertificate2(files[1].getBytes());
                if (files.length >= 3) pending.setCertificate3(files[2].getBytes());
                if (files.length >= 4) pending.setCertificate4(files[3].getBytes());
                if (files.length == 5) pending.setCertificate5(files[4].getBytes());

                pendingUserRepository.save(pending);
                return ResponseEntity.ok(Map.of("message", "Registration submitted. Awaiting admin approval."));
            }

            // Nếu không cần duyệt (ví dụ: USER) thì lưu trực tiếp vào Users
            Users user = new Users();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUsername(request.getFullName());
            user.setFullname(request.getFullName());
            user.setIsApproved(true);

            Role role = new Role();
            role.setId(roleId);
            user.setRoleId(role);

            if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
                String avatarUrl = cloudinaryService.uploadFile(request.getAvatar());
                user.setAvatar(avatarUrl);
            }

            loginRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Registered successfully"));

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

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = loginRepository.findByEmail(email) != null;
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // === Get Pending Register ===
    @GetMapping("/pending-registrations")
    public ResponseEntity<?> listPendingUsers() {
        return ResponseEntity.ok(pendingUserRepository.findByIsReviewedFalse());
    }


    // === LOGIN ===
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        Map<String, String> result = userService.loginUsers(userDTO);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    @GetMapping("/by-role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(@PathVariable Integer roleId) {
        List<Users> users = loginRepository.findByRoleId_Id(roleId);

        List<Map<String, Object>> result = users.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("email", user.getEmail());
            map.put("fullname", user.getFullname());
            map.put("avatar", user.getAvatar());
            map.put("certificates", user.getCertificates());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getProfile(@PathVariable Integer id) {
        Optional<Users> optionalUser = loginRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        Users user = optionalUser.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("fullname", user.getFullname());
        response.put("username", user.getUsername());
        response.put("avatar", user.getAvatar());
        response.put("address", user.getAddress());
        response.put("dob", user.getDob());
        response.put("gender", user.getGender());
        response.put("role", user.getRoleId() != null ? user.getRoleId().getRoleName() : null);

        return ResponseEntity.ok(response);
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
                //Xóa ảnh cũ trên cloud
                if(user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    cloudinaryService.deleteFile(user.getAvatar());
                }
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