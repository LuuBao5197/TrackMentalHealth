package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Tests")
public class Test {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "instructions")
    private String instructions;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

/*
 TODO [Reverse Engineering] create field to map the 'updated_at' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "updated_at", columnDefinition = "timestamp not null")
    private Object updatedAt;
*/
}