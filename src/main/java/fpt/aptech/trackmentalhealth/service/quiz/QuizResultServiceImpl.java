package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizResult;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizResultServiceImpl implements QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    @Override
    public QuizResult createResult(QuizResult result) {
        return quizResultRepository.save(result);
    }

}
