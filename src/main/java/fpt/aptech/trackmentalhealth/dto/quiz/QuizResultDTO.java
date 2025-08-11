package fpt.aptech.trackmentalhealth.dto.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizResult;
import lombok.Data;

@Data
public class QuizResultDTO {
    private Integer id;
    private Integer minScore;
    private Integer maxScore;
    private String resultLabel;    // Ví dụ: "Không có trầm cảm", "Trầm cảm nặng"
    private String description;

    public QuizResultDTO() {
    }
    public QuizResultDTO(QuizResult quizResult) {
        this.id = quizResult.getId();
        this.minScore = quizResult.getMinScore();
        this.maxScore = quizResult.getMaxScore();
        this.resultLabel = quizResult.getResultLabel();
        this.description = quizResult.getDescription();
    }


    // Mô tả chi tiết hơn nếu muốn
}

