package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.UserQuizAnswerItem;
import fpt.aptech.trackmentalhealth.entities.UserQuizAnswerItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizAnswerItemRepository extends JpaRepository<UserQuizAnswerItem, UserQuizAnswerItemId> {}
