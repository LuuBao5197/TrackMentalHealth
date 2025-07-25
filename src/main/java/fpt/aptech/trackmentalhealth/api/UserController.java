package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.GetUserNameDTO;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Optional<Users> user = userRepository.findById(id);
        return user.map(u -> {
                    GetUserNameDTO dto = new GetUserNameDTO(u.getUsername(), u.getFullname());
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
