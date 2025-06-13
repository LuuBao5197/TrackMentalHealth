package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TestDesigners")
public class TestDesigner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Size(max = 255)
    @Column(name = "field_of_expertise")
    private String fieldOfExpertise;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "verified")
    private Boolean verified;

/*
 TODO [Reverse Engineering] create field to map the 'joined_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "joined_at", columnDefinition = "timestamp not null")
    private Object joinedAt;
*/
}