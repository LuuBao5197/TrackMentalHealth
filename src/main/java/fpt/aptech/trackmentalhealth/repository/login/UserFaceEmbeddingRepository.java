package fpt.aptech.trackmentalhealth.repository.login;

import fpt.aptech.trackmentalhealth.entities.UserFaceEmbedding;
import fpt.aptech.trackmentalhealth.entities.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFaceEmbeddingRepository extends JpaRepository<UserFaceEmbedding, Long> {
    // Lấy embedding theo user
    List<UserFaceEmbedding> findByUser(Users user);

    @Query("SELECT e FROM UserFaceEmbedding e WHERE e.user.username = :username")
    Optional<UserFaceEmbedding> findByUsername(@Param("username") String username);

    // Nếu chỉ lưu 1 embedding mỗi user
    Optional<UserFaceEmbedding> findFirstByUserOrderByCreatedAtDesc(Users user);

    // Tìm tất cả embedding của 1 userId
    List<UserFaceEmbedding> findByUser_Id(Integer userId);
}
