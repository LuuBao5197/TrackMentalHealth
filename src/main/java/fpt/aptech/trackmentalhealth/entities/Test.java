package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Tests")
public class Test implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @Column(name = "title", unique = true, columnDefinition = "NVARCHAR(MAX)")
    private String title;

    @Lob
    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Lob
    @Column(name = "instructions", columnDefinition = "NVARCHAR(MAX)")
    private String instructions;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private String status = "not_submitted";

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TestQuestion> questions;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TestResult> results;
}
