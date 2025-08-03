package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {}