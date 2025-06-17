package fpt.aptech.trackmentalhealth.service.diary;

import fpt.aptech.trackmentalhealth.entities.Diary;

import java.util.List;
import java.util.Optional;

public interface DiaryService {
    List<Diary> findAll();
    Optional<Diary> findById(Integer id);
    List<Diary> findByUserId(Integer userId);
    Diary save(Diary diary);
    void deleteById(Integer id);
}