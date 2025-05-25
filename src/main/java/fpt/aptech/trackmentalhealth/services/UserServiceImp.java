package fpt.aptech.trackmentalhealth.services;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repositories.LoginRepository;
import fpt.aptech.trackmentalhealth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Users register(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return loginRepository.save(user);
    }

    @Override
    public String loginUsers(UserDTO userDTO) {
        Users user = loginRepository.findByEmail(userDTO.getEmail());
        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getEmail()); // trả về JWT token
        }
        return null; // login thất bại
    }

    @Override
    public List<Users> findAllUsers() {
        return loginRepository.findAll();
    }
}
