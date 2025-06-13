package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ContentCreators")
public class ContentCreator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Lob
    @Column(name = "bio")
    private String bio;

    @Lob
    @Column(name = "expertise_tags")
    private String expertiseTags;

    @Column(name = "verified")
    private Boolean verified;

    @OneToMany(mappedBy = "createdBy")
    private Set<fpt.aptech.trackmentalhealth.entities.Exercise> exercises = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<fpt.aptech.trackmentalhealth.entities.Lesson> lessons = new LinkedHashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<fpt.aptech.trackmentalhealth.entities.UserLessonProgress> userLessonProgresses = new LinkedHashSet<>();

/*
 TODO [Reverse Engineering] create field to map the 'joined_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "joined_at", columnDefinition = "timestamp not null")
    private Object joinedAt;
*/
}