package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // Ví dụ: "Mức độ hiểu biết cao", "Cần cải thiện", v.v.

    private String description; // Giải thích chi tiết kết quả

    private Double minScore; // Điểm tối thiểu cho kết quả này
    private Double maxScore; // Điểm tối đa cho kết quả này

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}
