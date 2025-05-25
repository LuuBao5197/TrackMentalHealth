package fpt.aptech.trackmentalhealth.services;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<Users> findByEmail(String email);

    Users register(Users user);

    String loginUsers(UserDTO userDTO);

    List<Users> findAllUsers();
}
