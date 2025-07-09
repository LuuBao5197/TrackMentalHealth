package fpt.aptech.trackmentalhealth.repository.notification;

import fpt.aptech.trackmentalhealth.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("select n from Notification n where n.user.id=:userid")
    List<Notification> getNotificationByUserId(int userid);

    @Query("select n from Notification n where n.id=:id")
    Notification getNotificationById(int id);

}
