package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "duration_spent")
    private Integer durationSpent;

/*
 TODO [Reverse Engineering] create field to map the 'completed_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "completed_at", columnDefinition = "timestamp")
    private Object completedAt;
*/
}