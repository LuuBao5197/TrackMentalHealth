package fpt.aptech.trackmentalhealth.service.quiz;


import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizDetailDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizResultDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.quiz.QuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizQuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import fpt.aptech.trackmentalhealth.ultis.ConvertDTOtoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for (Quiz quiz : quizzes.getContent()) {
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.setId(quiz.getId());
            quizDTO.setTitle(quiz.getTitle());
            quizDTO.setDescription(quiz.getDescription());
            quizDTO.setNumberOfQuestions(quiz.getNumberOfQuestions());
            quizDTO.setTimeLimit(quiz.getTimeLimit());
            quizDTO.setHasResults(quiz.getQuizResults() != null && !quiz.getQuizResults().isEmpty());
            quizDTOList.add(quizDTO);
        }
        // ✅ Truyền thêm pageable và totalElements
        return new PageImpl<>(quizDTOList, pageable, quizzes.getTotalElements());
    }

    @Override
    public QuizDetailDTO findOne(Integer id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        QuizDetailDTO quizDetailDTO = new QuizDetailDTO();
        quizDetailDTO.setId(quiz.getId());
        quizDetailDTO.setTitle(quiz.getTitle());
        quizDetailDTO.setDescription(quiz.getDescription());
        quizDetailDTO.setNumberOfQuestions(quiz.getNumberOfQuestions());
        quizDetailDTO.setTimeLimit(quiz.getTimeLimit());
        quizDetailDTO.setQuizQuestions(getRandomQuestionsFromQuiz(quiz.getId()));
        List<QuizResult> quizzesList = quiz.getQuizResults();
        List<QuizResultDTO> quizResultDTOList = new ArrayList<>();
        for (QuizResult quizResult : quizzesList) {
            QuizResultDTO quizResultDTO = new QuizResultDTO(quizResult);
            quizResultDTOList.add(quizResultDTO);
        }
        quizDetailDTO.setQuizResults(quizResultDTOList);
        return quizDetailDTO;
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
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for (Quiz quiz : quizzes.getContent()) {
            QuizDTO quizDTO = new QuizDTO();
            quizDTO.setId(quiz.getId());
            quizDTO.setTitle(quiz.getTitle());
            quizDTO.setDescription(quiz.getDescription());
            quizDTO.setNumberOfQuestions(quiz.getNumberOfQuestions());
            quizDTO.setTimeLimit(quiz.getTimeLimit());
            quizDTO.setHasResults(quiz.getQuizResults() != null && !quiz.getQuizResults().isEmpty());
            quizDTOList.add(quizDTO);
        }
        return new PageImpl<>(quizDTOList, pageable, quizzes.getTotalElements());
    }

    @Override
    public List<QuestionDTO> getQuestionsFromQuiz(Integer quizId) {
        List<Question> questionList = quizQuestionRepository.getAllQuestionOfQuiz(quizId);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = ConvertDTOtoEntity.convertQuestionToQuestionDTO(question);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
    @Override
    public List<QuestionDTO> getRandomQuestionsFromQuiz(Integer quizId) {
        Quiz quiz
                = quizRepository.findById(quizId).orElseThrow();
        Integer numberQuestionGetted = quiz.getNumberOfQuestions();
        List<Question> questionList = quizQuestionRepository.getAllQuestionOfQuiz(quizId);

        // Shuffle danh sách để lấy ngẫu nhiên
        Collections.shuffle(questionList);

        // Giới hạn số lượng câu hỏi trả về
        int limit = Math.min(numberQuestionGetted, questionList.size());
        List<Question> selectedQuestions = questionList.subList(0, limit);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : selectedQuestions) {
            QuestionDTO questionDTO = ConvertDTOtoEntity.convertQuestionToQuestionDTO(question);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }


}
