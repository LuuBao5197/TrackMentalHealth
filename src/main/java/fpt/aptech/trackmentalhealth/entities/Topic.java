package fpt.aptech.trackmentalhealth.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name; // e.g., "Depression", "Anxiety", etc.

    private boolean deleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime deletedAt;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
//    @JsonBackReference
    private List<Question> questions;
}
