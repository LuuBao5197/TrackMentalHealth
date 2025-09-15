package fpt.aptech.trackmentalhealth.dto;

import lombok.Data;

@Data
public class FaceLoginRequest {
    private String email;
    private double[] embedding;
}
