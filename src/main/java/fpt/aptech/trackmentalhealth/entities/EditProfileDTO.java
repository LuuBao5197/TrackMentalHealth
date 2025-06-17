package fpt.aptech.trackmentalhealth.entities;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class EditProfileDTO {
    private String fullname;
    private String address;
    private LocalDate dob;
    private String gender;
    private MultipartFile avatar;
}
