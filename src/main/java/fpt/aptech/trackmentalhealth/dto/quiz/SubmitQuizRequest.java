package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

import java.util.List;

@Data
public class SubmitQuizRequest {
    private Integer quizId;
    private Integer userId;
    private List<QuestionAnswerDto> answers;


    @Data
    public static class QuestionAnswerDto {
        private Integer questionId;
        private String userInput; // For text/number input
        private List<Integer> selectedOptionIds; // For multi-choice
        // MATCHING: danh sách cặp ghép
        private List<MatchingPairDto> matchingPairs;

        // ORDERING: danh sách item + thứ tự user chọn
        private List<OrderingItemDto> orderingItems;
    }
    @Data
    public static class MatchingPairDto {
        private String leftText;
        private String rightText;
    }

    @Data
    public static class OrderingItemDto {
        private Integer itemId;
        private String text;
        private Integer userOrder;
    }
}
