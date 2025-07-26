package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.UserTestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserTestAnswerRepository extends JpaRepository<UserTestAnswer, Integer> {
    @Query(
            "SELECT u from UserTestAnswer u where u.question.id = :questionId and u.attempt.users.id =:userId"
    )
    UserTestAnswer findUserTestAnswerByQuestionIdAndUserId(Integer questionId, Integer userId);
}
