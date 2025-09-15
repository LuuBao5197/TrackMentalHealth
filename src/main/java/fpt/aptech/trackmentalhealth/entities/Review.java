package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // Người review
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Size(max = 255)
    @Nationalized
    @Column(name = "psychologist_code")
    private String psychologistCode;

    // Rating từ 1-5
    @Column(name = "rating")
    private Integer rating;

    // Comment / nhận xét
    @Lob
    @Column(name = "comment")
    private String comment;

    // Thời gian tạo review
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
