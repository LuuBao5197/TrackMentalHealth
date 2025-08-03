package fpt.aptech.trackmentalhealth.service.admin;


import fpt.aptech.trackmentalhealth.repository.admin.AdminStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminStatisticsService {

    @Autowired
    private AdminStatisticsRepository repository;

    public Map<String, Long> getAllStatistics() {
        return Map.of(
                "totalUsers", repository.countTotalUsers(),
                "totalPsychologists", repository.countTotalPsychologists(),
                "totalContentCreators", repository.countTotalContentCreators(),
                "totalTestDesigners", repository.countTotalTestDesigners(),
                "totalComments", repository.countTotalComments(),
                "totalArticles", repository.countTotalArticles(),
                "totalExercises", repository.countTotalExercises(),
                "totalLessons", repository.countTotalLessons()
        );
    }
}
