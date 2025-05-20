package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Psy_Spec")
public class PsySpec {
    @EmbeddedId
    private PsySpecId id;

    @MapsId("bCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bCode", nullable = false)
    private fpt.aptech.trackmentalhealth.entities.Psychologist bCode;

    @MapsId("sCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sCode", nullable = false)
    private fpt.aptech.trackmentalhealth.entities.Specialization sCode;

}