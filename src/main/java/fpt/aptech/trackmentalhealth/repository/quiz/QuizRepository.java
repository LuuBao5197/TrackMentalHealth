package fpt.aptech.trackmentalhealth.repository.quiz;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

}
