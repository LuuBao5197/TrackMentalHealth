package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizAttempt extends JpaRepository<UserQuizAnswer, Integer> {
}
