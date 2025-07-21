package fpt.aptech.trackmentalhealth.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Question {
//    public enum QuestionType {
//        SINGLE_CHOICE,     // 1 đáp án đúng
//        MULTIPLE_CHOICE,   // nhiều đáp án đúng
//        TEXT_INPUT         // người dùng tự nhập
//    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

//    @Enumerated(EnumType.STRING)
//    private QuestionType type;

    private String category;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

}


