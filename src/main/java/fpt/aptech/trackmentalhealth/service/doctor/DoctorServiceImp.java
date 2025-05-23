package fpt.aptech.trackmentalhealth.service.doctor;

import fpt.aptech.trackmentalhealth.dto.UserMoodDTO;
import fpt.aptech.trackmentalhealth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DoctorServiceImp implements DoctorService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserMoodDTO> getUserMoodHistory(Integer userId) {
        return userRepository.findUserMoodHistory(userId);
    }
}

