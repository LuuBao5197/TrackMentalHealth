package fpt.aptech.trackmentalhealth.ultis;

import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import fpt.aptech.trackmentalhealth.entities.Option;
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
}
