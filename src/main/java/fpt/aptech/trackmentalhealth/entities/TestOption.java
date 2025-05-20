package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TestOptions")
public class TestOption {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private fpt.aptech.trackmentalhealth.entities.TestQuestion question;

    @Lob
    @Column(name = "option_text")
    private String optionText;

    @Column(name = "score_value")
    private Integer scoreValue;

    @Column(name = "option_order")
    private Integer optionOrder;

}