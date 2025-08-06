package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Min(0)
    @Max(99)
    private Integer minScore;      // Điểm thấp nhất của thang
    @Min(1)
    @Max(100)
    private Integer maxScore;      // Điểm cao nhất của thang
    private String resultLabel;    // Ví dụ: "Không có trầm cảm", "Trầm cảm nặng"
    private String description;    // Mô tả chi tiết hơn nếu muốn

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;             // Liên kết với bài Quiz
}
