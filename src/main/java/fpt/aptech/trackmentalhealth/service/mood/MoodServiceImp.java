package fpt.aptech.trackmentalhealth.service.mood;

import fpt.aptech.trackmentalhealth.entities.Mood;
import fpt.aptech.trackmentalhealth.repository.mood.MoodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MoodServiceImp implements MoodService {

    private final MoodRepository moodRepository;

    public MoodServiceImp(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    @Override
    public List<Mood> findAll() {
        return moodRepository.findAll();
    }

    @Override
    public Optional<Mood> findById(Integer id) {
        return moodRepository.findById(id);
    }

    @Override
    public Mood save(Mood mood) {
        return moodRepository.save(mood);
    }

    @Override
    public void deleteById(Integer id) {
        moodRepository.deleteById(id);
    }

    @Override
    public List<Mood> findByUserId(Integer userId) {
        return moodRepository.findByUsersId(userId);
    }
    @Override
    public List<Mood> findByUserIdAndDate(Integer userId, LocalDate date) {
        return moodRepository.findByUsersIdAndDate(userId, date);
    }

}