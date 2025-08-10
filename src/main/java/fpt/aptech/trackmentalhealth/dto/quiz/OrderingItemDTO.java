package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

@Data
public class OrderingItemDTO {
    private Integer id;
    private String content;
    private Integer correctOrder;
}
