package fpt.aptech.trackmentalhealth.dto; // hoặc package bạn dùng

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressRequest {
    public int userId;
    public int lessonId;
    public int stepCompleted;

    // Có thể thêm constructor, getter/setter nếu bạn dùng Lombok hoặc cần thao tác
}
