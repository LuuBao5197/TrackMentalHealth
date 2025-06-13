package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "LessonSteps")
public class LessonStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private fpt.aptech.trackmentalhealth.entities.Lesson lesson;

    @Column(name = "step_number")
    private Integer stepNumber;

    @Size(max = 1000)
    @Nationalized
    @Column(name = "title", length = 1000)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Size(max = 50)
    @Column(name = "media_type", length = 50)
    private String mediaType;

    @Lob
    @Column(name = "media_url")
    private String mediaUrl;

}