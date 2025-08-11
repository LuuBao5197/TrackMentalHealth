package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class MatchingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, columnDefinition = "NVARCHAR(MAX)")
    private String leftItem;
    @Column(length = 255, columnDefinition = "NVARCHAR(MAX")
    private String rightItem;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
