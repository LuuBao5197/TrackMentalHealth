package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PsySpecId implements Serializable {
    private static final long serialVersionUID = -6738148533210152062L;
    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "bCode", nullable = false)
    private String bCode;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "sCode", nullable = false)
    private String sCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PsySpecId entity = (PsySpecId) o;
        return Objects.equals(this.sCode, entity.sCode) &&
                Objects.equals(this.bCode, entity.bCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sCode, bCode);
    }

}