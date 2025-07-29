package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CommunityPosts")
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private LocalDate createAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommunityPostMedia> mediaList = new LinkedHashSet<>();

    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private Set<PostComment> postComments = new LinkedHashSet<>();
    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private Set<PostReaction> postReactions = new LinkedHashSet<>();

/*
 TODO [Reverse Engineering] create field to map the 'created_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "created_at", columnDefinition = "timestamp")
    private Object createdAt;
*/
}