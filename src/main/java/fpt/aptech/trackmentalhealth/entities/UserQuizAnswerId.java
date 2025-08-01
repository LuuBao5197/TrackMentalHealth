package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserQuizAnswerId implements Serializable {
    private Long attemptId;
    private Long questionId;
}
