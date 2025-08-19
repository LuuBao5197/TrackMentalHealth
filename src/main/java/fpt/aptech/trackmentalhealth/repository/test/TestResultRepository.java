package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    @Query(
            "SELECT t from TestResult t where t.test.id = :id"
    )
    List<TestResult> getTestResultsByTestId(@Param("id")Integer id);

    @Query("SELECT tr FROM TestResult tr " +
            "WHERE tr.test.id = :testId " +
            "AND tr.category = :domain " +
            "AND :score BETWEEN tr.minScore AND tr.maxScore")
    Optional<TestResult> findByTestAndCategoryAndScore(@Param("testId") Integer testId,
                                                       @Param("domain") String domain,
                                                       @Param("score") Integer score);
}
