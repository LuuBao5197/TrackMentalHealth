package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.MatchingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchingItemRepository extends JpaRepository<MatchingItem, Long> {

}
