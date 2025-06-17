package fpt.aptech.trackmentalhealth.services;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.ultis.JwtUtils;
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
    public Users register(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return loginRepository.save(users);
    }

    @Override
    public String loginUsers(UserDTO userDTO) {
        Users users = loginRepository.findByEmail(userDTO.getEmail());
        if (users != null && passwordEncoder.matches(userDTO.getPassword(), users.getPassword())) {
            return jwtUtils.generateToken(users.getEmail()); // trả về JWT token
        }
        return null; // login thất bại
    }

    @Override
    public List<Users> findAllUsers() {
        return loginRepository.findAll();
    }
}