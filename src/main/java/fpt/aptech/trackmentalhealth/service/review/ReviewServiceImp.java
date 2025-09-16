package fpt.aptech.trackmentalhealth.service.review;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Review;
import fpt.aptech.trackmentalhealth.repository.review.ReviewRepository;
import fpt.aptech.trackmentalhealth.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImp implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AppointmentService appointmentService;
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        return appointmentService.getAppointmentById(id);
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }


    @Override
    public double getAverageRatingByPsychologist(int psyId) {
        List<Review> reviews = reviewRepository.getReviewByPsychologistCode(psyId);
        if (reviews.isEmpty()) return 0; // không có review trả về 0
        return reviews.stream()
                .mapToInt(Review::getRating) // lấy rating của từng review
                .average()                   // tính trung bình
                .orElse(0);                  // fallback
    }

    @Override
    public long getReviewCountByPsychologist(int psyId) {
        List<Review> reviews = reviewRepository.getReviewByPsychologistCode(psyId);
        return reviews.size(); // trả về tổng số review
    }


}
