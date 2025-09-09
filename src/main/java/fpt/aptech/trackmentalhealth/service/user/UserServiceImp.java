package fpt.aptech.trackmentalhealth.service.user;

import fpt.aptech.trackmentalhealth.dto.UserDTO;
import fpt.aptech.trackmentalhealth.entities.ContentCreator;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.contentcreator.ContentCreatorRepository;
import fpt.aptech.trackmentalhealth.repository.login.LoginRepository;
import fpt.aptech.trackmentalhealth.repository.login.UserFaceEmbeddingRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
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
    private ContentCreatorRepository contentCreatorRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFaceEmbeddingRepository userFaceEmbeddingRepository;

    @Autowired
    private FaceEmbeddingService faceEmbeddingService;
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
    public Users registerWithFace(Users users, String embeddingJson) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users saved = loginRepository.save(users);

        // lưu embedding
        faceEmbeddingService.saveEmbedding(saved, embeddingJson);

        return saved;
    }

    @Override
    public Map<String, String> loginUsers(UserDTO userDTO) {
        Users user = loginRepository.findByEmail(userDTO.getEmail());

        if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            Optional<ContentCreator> contentCreatorOpt = contentCreatorRepository.findByUser(user);
            Integer contentCreatorId = contentCreatorOpt.map(ContentCreator::getId).orElse(null);

            String token = jwtUtils.generateToken(user.getEmail(), user, contentCreatorId);

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

    @Override
    public Users findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Map<String, String> loginUsersByFaceId(String email) {
        Users user = loginRepository.findByEmail(email);

        if (user != null) {
            // token chỉ cần email + user + role
            String token = jwtUtils.generateToken(user.getEmail(), user, null);

            return Map.of(
                    "token", token,
                    "roleName", user.getRoleId().getRoleName()
            );
        }
        return null;
    }


//    @Override
//    public Users registerWithFace(Users users, String embeddingJson) {
//        return null;
//    }
}