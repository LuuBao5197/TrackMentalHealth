package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ExpertReviewVotes")
public class ExpertReviewVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "expert_user_id")
    private Integer expertUserId;

    @Size(max = 50)
    @Column(name = "vote", length = 50)
    private String vote;

    @Lob
    @Column(name = "comment")
    private String comment;

/*
 TODO [Reverse Engineering] create field to map the 'vote_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "vote_at", columnDefinition = "timestamp")
    private Object voteAt;
*/
}