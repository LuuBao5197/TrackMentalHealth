package fpt.aptech.trackmentalhealth.service.mood;

import fpt.aptech.trackmentalhealth.entities.Mood;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodService {
    List<Mood> findAll();
    Optional<Mood> findById(Integer id);
    Mood save(Mood mood);
    void deleteById(Integer id);
    List<Mood> findByUserId(Integer userId);
    List<Mood> findByUserIdAndDate(Integer userId, LocalDate date);

}