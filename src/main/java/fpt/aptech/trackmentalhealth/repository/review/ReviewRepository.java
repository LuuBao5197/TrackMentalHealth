package fpt.aptech.trackmentalhealth.repository.review;

import fpt.aptech.trackmentalhealth.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    @Query("select r from Review r where r.psychologistCode = :PsyId")
    List<Review> getReviewByPsychologistCode(int PsyId);

}
