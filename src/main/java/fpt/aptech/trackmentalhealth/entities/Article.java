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
@Table(name = "Article", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Article__E52A1BB32A568961", columnNames = {"title"})
})
public class Article {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private fpt.aptech.trackmentalhealth.entities.User author;

    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 255)
    @Nationalized
    @Column(name = "status")
    private String status;

/*
 TODO [Reverse Engineering] create field to map the 'update_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "update_at", columnDefinition = "timestamp not null")
    private Object updateAt;
*/
}