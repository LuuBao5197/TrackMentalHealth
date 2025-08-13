package fpt.aptech.trackmentalhealth.entities;

import fpt.aptech.trackmentalhealth.entities.Exercise;
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

    private String type;

    private String description;

    private String duration;

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
