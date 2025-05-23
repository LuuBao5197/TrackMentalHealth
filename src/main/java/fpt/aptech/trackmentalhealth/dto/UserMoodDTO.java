package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.Test;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMoodDTO {
    // User fields
    private Integer userId;
    private String username;
    private String fullname;
    private String gender;
    private LocalDate dob;

    // Mood fields
    private Integer moodId;
    private LocalDate date;
    private String moodLevel; // assuming MoodLevel has a name
    private String note;

    // Test
    private Integer id;
    private Test test;
    private Instant startedAt;
    private Instant completedAt;
    private Integer totalScore;
    private String resultSummary;
}
