package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Review;
import fpt.aptech.trackmentalhealth.service.appointment.AppointmentService;
import fpt.aptech.trackmentalhealth.service.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AppointmentService appointmentService;

    // ====== Lấy tất cả review ======
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // ====== Tạo review mới ======
    @PostMapping("/appointment/{appointmentId}")
    public Review createReview(@RequestBody Review review, @PathVariable Integer appointmentId) {
        review.setCreatedAt(LocalDateTime.now());

        Appointment appointment = reviewService.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        appointment.setReview(review);

        // Lưu review
        Review savedReview = reviewService.createReview(review);

        // Lưu appointment để cập nhật review_id trong bảng appointment
        appointmentService.updateAppointment(appointment);

        return savedReview;
    }

    @GetMapping("/average/{psyId}")
    public double getAverageRatingByPsychologist(@PathVariable int psyId) {
        try {
            System.out.println("Fetching average rating for psychologist ID: " + psyId);
            double avg = reviewService.getAverageRatingByPsychologist(psyId);
            System.out.println("Average rating: " + avg);
            return avg;
        } catch (Exception e) {
            System.err.println("Error fetching average rating for psychologist ID " + psyId);
            e.printStackTrace();
            // Có thể trả giá trị mặc định hoặc ném exception 500
            return 0.0;
        }
    }

}
