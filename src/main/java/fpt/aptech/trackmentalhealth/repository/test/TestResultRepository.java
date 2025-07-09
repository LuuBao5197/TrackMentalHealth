package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    @Query(
            "SELECT t from TestResult t where t.test.id = :id"
    )
    List<TestResult> getTestResultsByTestId(@Param("id")Integer id);
}
