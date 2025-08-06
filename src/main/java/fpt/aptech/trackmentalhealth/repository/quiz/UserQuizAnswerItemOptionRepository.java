package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.UserQuizAnswerItemOption;
import fpt.aptech.trackmentalhealth.entities.UserQuizAnswerItemOptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizAnswerItemOptionRepository extends JpaRepository<UserQuizAnswerItemOption, UserQuizAnswerItemOptionId> {}
