package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserTestAnswerId implements Serializable {
    private static final long serialVersionUID = -803334747601705993L;
    @NotNull
    @Column(name = "attempt_id", nullable = false)
    private Integer attemptId;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @NotNull
    @Column(name = "selected_option_id", nullable = false)
    private Integer selectedOptionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserTestAnswerId entity = (UserTestAnswerId) o;
        return Objects.equals(this.questionId, entity.questionId) &&
                Objects.equals(this.selectedOptionId, entity.selectedOptionId) &&
                Objects.equals(this.attemptId, entity.attemptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, selectedOptionId, attemptId);
    }

}