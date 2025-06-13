package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ContentReports")
public class ContentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private Users reportedBy;

    @Size(max = 50)
    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "content_id")
    private Integer contentId;

    @Lob
    @Column(name = "reason")
    private String reason;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

/*
 TODO [Reverse Engineering] create field to map the 'reported_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "reported_at", columnDefinition = "timestamp")
    private Object reportedAt;
*/
}