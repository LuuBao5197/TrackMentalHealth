package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PostReactions")
public class PostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private CommunityPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Size(max = 50)
    @Column(name = "emoji_type", length = 50)
    private String emojiType;

/*
 TODO [Reverse Engineering] create field to map the 'reacted_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "reacted_at", columnDefinition = "timestamp")
    private Object reactedAt;
*/
}