package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PostComments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "post_id", nullable = false)
    private CommunityPost post;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

/*
 TODO [Reverse Engineering] create field to map the 'created_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "created_at", columnDefinition = "timestamp")
    private Object createdAt;
*/
}