package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Moods")
public class Mood {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private fpt.aptech.trackmentalhealth.entities.User user;

    @Column(name = "\"date\"")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_level")
    private MoodLevel moodLevel;

    @Lob
    @Column(name = "note")
    private String note;

}