package fpt.aptech.trackmentalhealth.service.user;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.Users;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<Users> findByEmail(String email);

    Users register(Users users);

    Map<String, String> loginUsers(UserDTO userDTO);

    List<Users> findAllUsers();

    Users findById(String id);

    Map<String, String> loginUsersByFaceId(Integer userId);

    Users registerWithFace(Users users, String embeddingJson);
}