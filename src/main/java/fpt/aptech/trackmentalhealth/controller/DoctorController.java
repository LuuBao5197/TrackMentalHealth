package fpt.aptech.trackmentalhealth.controller;


import fpt.aptech.trackmentalhealth.dto.UserMoodDTO;
import fpt.aptech.trackmentalhealth.service.doctor.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserMoodDTO>> getUserMoodHistory(@PathVariable Integer userId) {
        List<UserMoodDTO> moodHistory = doctorService.getUserMoodHistory(userId);
        return ResponseEntity.ok(moodHistory);
    }
}
