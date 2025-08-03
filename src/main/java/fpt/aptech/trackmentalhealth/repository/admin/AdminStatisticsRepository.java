package fpt.aptech.trackmentalhealth.repository.admin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminStatisticsRepository extends JpaRepository<fpt.aptech.trackmentalhealth.entities.Users, Integer> {

    @Query("SELECT COUNT(u) FROM Users u")
    long countTotalUsers();

    @Query("SELECT COUNT(p) FROM Psychologist p")
    long countTotalPsychologists();

    @Query("SELECT COUNT(c) FROM ContentCreator c")
    long countTotalContentCreators();

    @Query("SELECT COUNT(t) FROM TestDesigner t")
    long countTotalTestDesigners();

    @Query("SELECT COUNT(c) FROM Comment c")
    long countTotalComments();

    @Query("SELECT COUNT(a) FROM Article a")
    long countTotalArticles();

    @Query("SELECT COUNT(e) FROM Exercise e")
    long countTotalExercises();

    @Query("SELECT COUNT(l) FROM Lesson l")
    long countTotalLessons();
}
