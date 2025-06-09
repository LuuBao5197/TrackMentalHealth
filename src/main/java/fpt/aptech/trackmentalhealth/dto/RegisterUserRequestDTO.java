package fpt.aptech.trackmentalhealth.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterUserRequestDTO {
    private String email;
    private String password;
    private String fullName;
    private Integer roleId;
    private MultipartFile[] certificates;
}
