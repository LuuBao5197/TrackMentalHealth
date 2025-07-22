package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Lob
    @Column(name = "instruction")
    private String instruction;

    @Lob
    @Column(name = "media_url")
    private String mediaUrl;

    @Size(max = 50)
    @Column(name = "media_type", length = 50)
    private String mediaType;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private ContentCreator createdBy;

    @Column(name = "photo")
    private String photo;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "exercise")
    private Set<fpt.aptech.trackmentalhealth.entities.UserExerciseLog> userExerciseLogs = new LinkedHashSet<>();

}