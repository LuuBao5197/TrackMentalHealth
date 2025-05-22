package fpt.aptech.trackmentalhealth.repository;

import fpt.aptech.trackmentalhealth.dto.UserMoodDTO;
import fpt.aptech.trackmentalhealth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
        SELECT new fpt.aptech.trackmentalhealth.dto.UserMoodDTO(
            u.id, u.username, u.fullname, u.gender, u.dob,
            m.id, m.date, ml.name, m.note,t.id,t.test,t.startedAt,t.completedAt,t.totalScore,t.resultSummary
        )
        FROM User u
        JOIN Mood m ON u.id = m.user.id
        JOIN MoodLevel ml ON m.moodLevel.id = ml.id
        JOIN UserTestAttempt t ON u.id = t.user.id
        WHERE u.id = :userId
        ORDER BY m.date DESC
    """)
    List<UserMoodDTO> findUserMoodHistory(@Param("userId") Integer userId);
}
