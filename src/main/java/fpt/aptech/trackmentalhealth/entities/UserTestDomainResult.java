package fpt.aptech.trackmentalhealth.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "UserTestDomainResults")
public class UserTestDomainResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // Liên kết với attempt
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id")
    private UserTestAttempt attempt;

    // Tên domain (e.g., "Depression", "Anxiety", "Stress")
    @Column(name = "domain_name")
    private String domainName;

    // Điểm của domain này
    @Column(name = "score")
    private Integer score;

    // Kết quả đánh giá (ví dụ: Mild, Moderate, Severe)
    @Column(name = "result_text")
    private String resultText;
}
