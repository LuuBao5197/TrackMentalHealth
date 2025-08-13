package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.UserQuizAttempt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizAttemptRepository extends JpaRepository<UserQuizAttempt, Integer> {
    List<UserQuizAttempt> findByUserId(Integer userId);
//    @EntityGraph(attributePaths = {
//            "quiz",
//            "answerItems",
//            "answerItems.question",
//            "answerItems.selectedOptions.option",
//            "answerItems.matchingAnswers",
//            "answerItems.orderingAnswers"
//    })
//    Optional<UserQuizAttempt> findByIdWithAnswers(Integer id);
}
