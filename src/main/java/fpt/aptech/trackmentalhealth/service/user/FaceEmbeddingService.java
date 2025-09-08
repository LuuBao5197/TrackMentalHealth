package fpt.aptech.trackmentalhealth.service.user;

import fpt.aptech.trackmentalhealth.entities.UserFaceEmbedding;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.login.UserFaceEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaceEmbeddingService {

    private final UserFaceEmbeddingRepository embeddingRepository;

    public void saveEmbedding(Users user, String embeddingJson) {
        UserFaceEmbedding embedding = new UserFaceEmbedding();
        embedding.setUser(user);
        embedding.setEmbedding(embeddingJson);
        embeddingRepository.save(embedding);
    }

    public List<UserFaceEmbedding> findByUser(Users user) {
        return embeddingRepository.findByUser_Id(user.getId());
    }
}
