package fpt.aptech.trackmentalhealth.dto;

import lombok.Data;

@Data
public class FaceLoginRequest {
    private String username;
    private double[] embedding;
}
