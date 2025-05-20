package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesions_id")
    private ChatSession sesions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private fpt.aptech.trackmentalhealth.entities.User user;

    @Size(max = 255)
    @Nationalized
    @Column(name = "psychologist_code")
    private String psychologistCode;

    @Column(name = "rating")
    private Integer rating;

    @Lob
    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private Instant createdAt;

}