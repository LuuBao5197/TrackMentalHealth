package fpt.aptech.trackmentalhealth.service.user;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<Users> findByEmail(String email);

    Users register(Users users);

    String loginUsers(UserDTO userDTO);

    List<Users> findAllUsers();
}