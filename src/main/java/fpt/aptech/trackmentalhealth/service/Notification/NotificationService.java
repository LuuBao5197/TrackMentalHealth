package fpt.aptech.trackmentalhealth.service.Notification;

import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.entities.Users;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsByUserId(int userId);

    Notification getNotificationById(int id);

    Notification createNotification(Notification notification);

    // Thêm method mới
    Notification createNotification(Users user, String message);

    void deleteNotification(int id);

    void changeStatus(int id);
}
