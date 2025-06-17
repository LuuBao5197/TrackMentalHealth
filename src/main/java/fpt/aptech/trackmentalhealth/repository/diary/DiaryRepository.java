package fpt.aptech.trackmentalhealth.repository.diary;

import fpt.aptech.trackmentalhealth.entities.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findByUsers_Id(Integer userId); // Lấy tất cả nhật ký theo userId
}
