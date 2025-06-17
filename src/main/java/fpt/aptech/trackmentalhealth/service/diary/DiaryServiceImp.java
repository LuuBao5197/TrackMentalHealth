package fpt.aptech.trackmentalhealth.service.diary;

import fpt.aptech.trackmentalhealth.entities.Diary;
import fpt.aptech.trackmentalhealth.repository.diary.DiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryServiceImp implements DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryServiceImp(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Override
    public List<Diary> findAll() {
        return diaryRepository.findAll();
    }

    @Override
    public Optional<Diary> findById(Integer id) {
        return diaryRepository.findById(id);
    }

    @Override
    public List<Diary> findByUserId(Integer userId) {
        return diaryRepository.findByUsers_Id(userId);
    }

    @Override
    public Diary save(Diary diary) {
        return diaryRepository.save(diary);
    }

    @Override
    public void deleteById(Integer id) {
        diaryRepository.deleteById(id);
    }
}