package fpt.aptech.trackmentalhealth.ultis;

import fpt.aptech.trackmentalhealth.dto.quiz.MatchingItemDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.MatchingItem;
import fpt.aptech.trackmentalhealth.entities.Option;
import fpt.aptech.trackmentalhealth.entities.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ConvertDTOtoEntity {

    public static List<OptionDTO> convertOptionsToOptionDTO(List<Option> options) {
        List<OptionDTO> optionDTOs = new ArrayList<OptionDTO>();
        for (Option option : options) {
            OptionDTO optionDTO = new OptionDTO(option);
            optionDTOs.add(optionDTO);
        }
        return optionDTOs;
    }
    public static QuestionDTO convertQuestionToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setType(question.getType());
        questionDTO.setContent(question.getContent());
        if (question.getOptions() != null) {
            List<OptionDTO> optionDTOs = convertOptionsToOptionDTO(question.getOptions());
            questionDTO.setOptions(optionDTOs);
        }
        if (question.getMatchingItems() != null && question.getType().equals("MATCHING")) {
            List<MatchingItem> matchingItems = question.getMatchingItems();
            List<MatchingItemDTO> matchingItemDTOS  = new ArrayList<>();
            for (MatchingItem matchingItem : matchingItems) {
                MatchingItemDTO matchingItemDTO = new MatchingItemDTO();
                matchingItemDTO.setLeftItem(matchingItem.getLeftItem());
                matchingItemDTO.setRightItem(matchingItem.getRightItem());
                matchingItemDTOS.add(matchingItemDTO);
            }
            questionDTO.setMatchingItems(matchingItemDTOS);
        }
        return questionDTO;
    }

}
