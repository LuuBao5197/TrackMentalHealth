package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserQuizAnswerItemId implements Serializable {
    private Integer attemptId;
    private Integer questionId;
}