package fpt.aptech.trackmentalhealth.dto.test.history;

import fpt.aptech.trackmentalhealth.dto.test.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTestDetailDTO {
    private String questionInstruction;
    private String questionText;
    private List<OptionDTO> options;
    private String selectedOptionText;
    private Integer scoreValue;
//    private String resultLabel;
}
