package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizResult;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultServiceImpl implements QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;
    @Autowired
    private QuizRepository quizRepository;


    @Override
    public QuizResult createResult(QuizResult result) {
        return quizResultRepository.save(result);
    }

    @Override
    public List<QuizResult> createMultiResult(List<QuizResult> results) {
        for (QuizResult result : results) {
           Integer QuizID = result.getQuiz().getId();
           Quiz quiz = quizRepository.findById(QuizID).orElse(null);
           if (quiz != null) {
               result.setQuiz(quiz);
           }
        }
        return quizResultRepository.saveAll(results);
    }

}
