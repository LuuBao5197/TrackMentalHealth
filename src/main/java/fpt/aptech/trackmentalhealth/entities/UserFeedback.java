package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Lob
    @Column(name = "message")
    private String message;

/*
 TODO [Reverse Engineering] create field to map the 'submitted_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "submitted_at", columnDefinition = "timestamp")
    private Object submittedAt;
*/
}