package fpt.aptech.trackmentalhealth.repository.mood;

import fpt.aptech.trackmentalhealth.entities.Mood;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Integer> {
    List<Mood> findByUsersId(Integer userId);
    List<Mood> findByUsersIdAndDate(Integer userId, LocalDate date);
    Page<Mood> findByUsersId(Integer userId, Pageable pageable);

}