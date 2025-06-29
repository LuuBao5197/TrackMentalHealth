package fpt.aptech.trackmentalhealth.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonStepDto {
    private Integer id; // null nếu là tạo mới
    private Integer stepNumber;
    private String title;
    private String content;
    private String mediaType;
    private String mediaUrl;
}
