package fpt.aptech.trackmentalhealth.ultis;

import fpt.aptech.trackmentalhealth.dto.quiz.MatchingItemDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.OrderingItemDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.MatchingItem;
import fpt.aptech.trackmentalhealth.entities.Option;
import fpt.aptech.trackmentalhealth.entities.OrderingItem;
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
    public static List<OrderingItemDTO> convertOrderingItemsToOrderingItemDTO(List<OrderingItem> orderingItems) {
        List<OrderingItemDTO> orderingItemDTOS = new ArrayList<>();
        for (OrderingItem orderingItem : orderingItems) {
            OrderingItemDTO orderingItemDTO = new OrderingItemDTO();
            orderingItemDTO.setId(orderingItem.getId());
            orderingItemDTO.setContent(orderingItem.getContent());
            orderingItemDTO.setCorrectOrder(orderingItem.getCorrectOrder());
            orderingItemDTOS.add(orderingItemDTO);
        }
        return orderingItemDTOS;
    }

    public static List<MatchingItemDTO> convertMatchingItemsToMatchingItemDTO(List<MatchingItem> matchingItems) {
        List<MatchingItemDTO> matchingItemDTOS = new ArrayList<>();
        for (MatchingItem matchingItem : matchingItems) {
            MatchingItemDTO matchingItemDTO = new MatchingItemDTO();
            matchingItemDTO.setLeftItem(matchingItem.getLeftItem());
            matchingItemDTO.setRightItem(matchingItem.getRightItem());
            matchingItemDTOS.add(matchingItemDTO);
        }
        return matchingItemDTOS;

    }
    public static QuestionDTO convertQuestionToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setType(question.getType());
        questionDTO.setContent(question.getContent());
        questionDTO.setTopicName(question.getTopic().getName());
        questionDTO.setDifficulty(question.getDifficulty());
        questionDTO.setScore(question.getScore());
        if (question.getOptions() != null) {
            List<OptionDTO> optionDTOs = convertOptionsToOptionDTO(question.getOptions());
            questionDTO.setOptions(optionDTOs);
        }
        if (question.getMatchingItems() != null && question.getType().equals("MATCHING")) {
            List<MatchingItem> matchingItems = question.getMatchingItems();
            List<MatchingItemDTO> matchingItemDTOS  = convertMatchingItemsToMatchingItemDTO(matchingItems);
            questionDTO.setMatchingItems(matchingItemDTOS);
        }
        if (question.getOrderingItems() != null && question.getType().equals("ORDERING")) {
            questionDTO.setOrderingItems(convertOrderingItemsToOrderingItemDTO(question.getOrderingItems()));
        }
        return questionDTO;
    }

}
