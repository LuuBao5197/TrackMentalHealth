package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("SELECT q FROM Question q " +
            "WHERE (:keyword IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:topicId IS NULL OR q.topic.id = :topicId) " +
            "AND (:type IS NULL OR q.type = :type)")
    Page<Question> searchWithFilters(@Param("keyword") String keyword,
                                     @Param("topicId") Integer topicId,
                                     @Param("type") String type,
                                     Pageable pageable);

    @Query(
            "select q from Question q where q.id =:id"
    )
    Question getQuestionById(@Param("id") Integer id);
}