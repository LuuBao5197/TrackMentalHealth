package fpt.aptech.trackmentalhealth.repository.test;

import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Integer> {
    @Query(
            "select t from TestQuestion t where t.test.id =:id"
    )
    List<TestQuestion> getTestQuestionsByTestId(@Param("id") Integer id);
}
