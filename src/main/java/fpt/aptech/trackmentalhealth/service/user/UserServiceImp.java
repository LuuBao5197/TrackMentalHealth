package fpt.aptech.trackmentalhealth.service.user;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.ultis.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Optional<Users> findByEmail(String email) {
        return Optional.ofNullable(loginRepository.findByEmail(email));
    }

    @Override
    public Users register(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return loginRepository.save(users);
    }

    @Override
    public Map<String, String> loginUsers(UserDTO userDTO) {
        Users user = loginRepository.findByEmail(userDTO.getEmail());

        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateToken(user.getEmail(), user);
            return Map.of(
                    "token", token,
                    "roleName", user.getRoleId().getRoleName()
            );
        }

        return null;
    }


    @Override
    public List<Users> findAllUsers() {
        return loginRepository.findAll();
    }
}