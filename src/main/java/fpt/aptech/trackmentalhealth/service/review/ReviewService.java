package fpt.aptech.trackmentalhealth.service.review;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews();

    Appointment getAppointmentById(Integer id);


    Review createReview(Review review);

    double getAverageRatingByPsychologist(int psyId);

    // Mới: lấy tổng số review
    long getReviewCountByPsychologist(int psyId);
}
