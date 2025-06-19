package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Entity
public class Psychologist {
    @Id
    @Size(max = 255)
    @Nationalized
    @Column(name = "bCode", nullable = false)
    private String bCode;

    @Size(max = 255)
    @Nationalized
    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "experience_years")
    private Double experienceYears;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private Users usersID;

    @Column(name = "bio")
    private Integer bio;

}