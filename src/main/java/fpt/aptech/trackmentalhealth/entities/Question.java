package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)")
    private String content;
    private String type; // MULTI_CHOICE, TEXT_INPUT, NUMBER_INPUT, SCORE_BASED, MATCHING, ORDERING
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private DifficultLevel difficulty; // EASY, MEDIUM, HARD
    private Integer score;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> quizQuestions = new ArrayList<>();

//    @JsonManagedReference
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;
   // Doi voi loai cau hoi MATCHING
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchingItem> matchingItems = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderingItem> orderingItems = new ArrayList<>();
}
