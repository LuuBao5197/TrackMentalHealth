package fpt.aptech.trackmentalhealth.service.quiz;


import fpt.aptech.trackmentalhealth.dto.quiz.QuizDTO;
import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.entities.QuizQuestionId;
import fpt.aptech.trackmentalhealth.repository.quiz.QuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizQuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Override
    public Page<QuizDTO> findAll(Pageable pageable) {
        Page<Quiz> quizzes = quizRepository.findAll(pageable);
        List<Quiz> quizzesList = quizzes.getContent();
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for (Quiz quiz : quizzesList) {
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.setId(quiz.getId());
            quizDTO.setTitle(quiz.getTitle());
            quizDTO.setDescription(quiz.getDescription());
            quizDTO.setNumberOfQuestions(quiz.getNumberOfQuestions());
            quizDTO.setTimeLimit(quiz.getTimeLimit());
            quizDTO.setHasResults(quiz.getQuizResults() != null);
            quizDTOList.add(quizDTO);
        }
        Page<QuizDTO> quizDTOPage = new PageImpl<>(quizDTOList);
        return quizDTOPage;
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        if(quiz.getQuizQuestions() != null) {
            quiz.getQuizQuestions().forEach(question -> question.setQuiz(quiz));
        } else {
            return null;
        }
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz assignQuestionsToQuiz(Integer quizId, List<Integer> questionIds) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();

        for (Integer qId : questionIds) {
            Question question = questionRepository.findById(qId).orElseThrow();
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setId(new QuizQuestionId(quizId, qId));
            quizQuestion.setQuiz(quiz);
            quizQuestion.setQuestion(question);
            quizQuestionRepository.save(quizQuestion);
        }

        return quiz;
    }

    @Override
    public Page<QuizDTO> searchQuizzes(String keyword, Pageable pageable) {
        Page<Quiz> quizzes = quizRepository.searchQuiz(keyword, pageable);
        List<Quiz> quizzesList = quizzes.getContent();
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for (Quiz quiz : quizzesList) {
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.setId(quiz.getId());
            quizDTO.setTitle(quiz.getTitle());
            quizDTO.setDescription(quiz.getDescription());
            quizDTO.setNumberOfQuestions(quiz.getNumberOfQuestions());
            quizDTO.setTimeLimit(quiz.getTimeLimit());
            quizDTO.setHasResults(quiz.getQuizResults() != null);
            quizDTOList.add(quizDTO);
        }
        Page<QuizDTO> quizDTOPage = new PageImpl<>(quizDTOList);
        return quizDTOPage;
    }
}
