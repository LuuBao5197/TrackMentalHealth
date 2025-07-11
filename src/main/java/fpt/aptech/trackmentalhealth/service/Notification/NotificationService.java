package fpt.aptech.trackmentalhealth.service.Notification;

import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface NotificationService {
    List<Notification> getNotificationsByUserId(int userId);
    Notification getNotificationById(int id);
    Notification createNotification(Notification notification);
    void deleteNotification(int id);
}
