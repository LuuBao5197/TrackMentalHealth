package fpt.aptech.trackmentalhealth.service.doctor;

import fpt.aptech.trackmentalhealth.dto.UserMoodDTO;
import fpt.aptech.trackmentalhealth.entities.User;

import java.util.List;

public interface DoctorService {
    List<UserMoodDTO> getUserMoodHistory(Integer userId);
}
