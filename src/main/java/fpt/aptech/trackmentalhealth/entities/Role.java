package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Roles", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__Roles__B1947861B9D11836", columnNames = {"roleName"})
})
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Nationalized
    @Column(name = "roleName")
    private String roleName;

}