package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_face_embeddings")
public class UserFaceEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với Users
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Lưu embedding dưới dạng JSON string (hoặc Base64)
    @Column(name = "embedding", columnDefinition = "NVARCHAR(MAX)")
    private String embedding;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
