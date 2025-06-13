package fpt.aptech.trackmentalhealth.repository;

import fpt.aptech.trackmentalhealth.entities.PendingUserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingUserRepository extends JpaRepository<PendingUserRegistration, Integer> {
    List<PendingUserRegistration> findByIsReviewedFalse();
}
