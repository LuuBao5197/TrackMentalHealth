package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Specialization", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Speciali__72E12F1B9BEA763F", columnNames = {"name"})
})
public class Specialization {
    @Id
    @Size(max = 255)
    @Nationalized
    @Column(name = "code", nullable = false)
    private String code;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name")
    private String name;

}