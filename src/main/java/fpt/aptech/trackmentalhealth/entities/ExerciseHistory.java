package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user; // Liên kết với người dùng thực hiện bài tập

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise; // Liên kết với bài tập

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // Thời gian hoàn thành

    private String status; // VD: COMPLETED, FAILED, IN_PROGRESS

    private Integer score; // Điểm số (nếu có hệ thống chấm điểm)

    private String feedback; // Phản hồi từ hệ thống (nếu có)

    @Column(name = "difficulty_level")
    private String difficultyLevel; // Độ khó của bài tập khi thực hiện
}