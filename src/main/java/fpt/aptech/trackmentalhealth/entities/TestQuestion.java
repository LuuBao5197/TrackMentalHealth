package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TestQuestions")
public class TestQuestion {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private fpt.aptech.trackmentalhealth.entities.Test test;

    @Lob
    @Column(name = "question_text")
    private String questionText;

    @Size(max = 10)
    @Column(name = "question_type", length = 10)
    private String questionType;

    @Column(name = "question_order")
    private Integer questionOrder;

}