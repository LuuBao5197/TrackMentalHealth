package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizResult;

import java.util.List;

public interface QuizResultService {
    QuizResult createResult(QuizResult result);
    List<QuizResult> createMultiResult(List<QuizResult> results);
}
