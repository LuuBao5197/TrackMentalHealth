package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.TestOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestOptionRepository extends JpaRepository<TestOption, Integer> {
    @Query(
            "SELECT t from TestOption t where t.question.id = :id"
    )
    List<TestOption> getTestOptionsByTestQuestionId(@Param("id") Integer id);
}
