package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_conditions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // VD: LEFT_HAND_UP, RIGHT_HAND_UP

    private String description; // VD: Raise your left hand for 4 seconds

    private String duration; // Số giây hoặc thời gian thực hiện

    @Column(name = "step_order")
    private Integer stepOrder; // Bước số mấy

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @PrePersist
    @PreUpdate
    private void validateExerciseMediaType() {
        if (exercise != null && (exercise.getMediaType() == null
                || !exercise.getMediaType().equalsIgnoreCase("camera"))) {
            throw new IllegalStateException("Cannot add condition to exercise without mediaType = 'camera'");
        }
    }
}
