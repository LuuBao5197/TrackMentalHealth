package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TestResults",  uniqueConstraints = @UniqueConstraint(columnNames = {"test_id", "min_score", "max_score"}))
public class TestResult {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    @JsonBackReference
    private Test test;

    @Column(name = "min_score")
    private Integer minScore;

    @Column(name = "max_score")
    private Integer maxScore;

    @Lob
    @Column(name = "result_text")
    private String resultText;
}
