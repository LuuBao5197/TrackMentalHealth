package fpt.aptech.trackmentalhealth.repository.mood;

import fpt.aptech.trackmentalhealth.entities.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Integer> {
    List<Mood> findByUsersId(Integer userId);
    List<Mood> findByUsersIdAndDate(Integer userId, LocalDate date);
}