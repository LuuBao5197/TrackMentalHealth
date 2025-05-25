package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private String otp;
}
