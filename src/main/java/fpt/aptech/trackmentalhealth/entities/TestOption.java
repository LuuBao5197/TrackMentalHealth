package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private TestQuestion question;

    @Lob
    @Column(name = "option_text", columnDefinition = "NVARCHAR(MAX)")
    private String optionText;

    @Column(name = "score_value")
    private Integer scoreValue;

    @Column(name = "option_order")
    private Integer optionOrder;
}
