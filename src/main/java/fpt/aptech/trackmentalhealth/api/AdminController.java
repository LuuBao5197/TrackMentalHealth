package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class AdminController {

    @Autowired
    UserService userService;

    // === PROFILE ===
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<Users> userOpt = userService.findByEmail(email);
        return userOpt
                .map(user -> ResponseEntity.ok(Map.of(
                        "fullname", user.getFullname(),
                        "email", user.getEmail(),
                        "dob", user.getDob(),
                        "gender", user.getGender(),
                        "avatar", user.getAvatar(),
                        "address", user.getAddress()
                )))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }
}
