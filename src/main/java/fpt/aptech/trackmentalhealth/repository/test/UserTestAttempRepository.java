package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.UserTestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTestAttempRepository extends JpaRepository<UserTestAttempt, Integer> {
    @Query(
            "select u from UserTestAttempt u where u.test.id = :testId and u.users.id =:userId ORDER BY u.completedAt DESC LIMIT 1"
    )
    UserTestAttempt findUserTestAttemptByTestIdAndUserId(Integer testId, Integer userId);
}
