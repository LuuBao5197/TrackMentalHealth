package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "Lessons")


public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 250)
    @Nationalized
    @Column(name = "title", length = 250)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private ContentCreator createdBy;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "lesson")
    private Set<LessonStep> lessonSteps = new LinkedHashSet<>();

    @OneToMany(mappedBy = "lesson")
    private Set<fpt.aptech.trackmentalhealth.entities.UserLessonProgress> userLessonProgresses = new LinkedHashSet<>();

}