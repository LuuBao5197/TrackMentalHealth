package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quiz_option")  // ✅ đổi tên bảng để tránh xung đột
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;
    @Column(name = "is_correct")
    private boolean correct;
    private Integer score;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}