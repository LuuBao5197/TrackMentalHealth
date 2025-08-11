package fpt.aptech.trackmentalhealth.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class OrderingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;
    private Integer correctOrder; // Thứ tự đúng (ví dụ: 0, 1, 2, 3)
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
