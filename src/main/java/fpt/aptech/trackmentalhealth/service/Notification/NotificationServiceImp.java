package fpt.aptech.trackmentalhealth.service.Notification;

import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NotificationServiceImp implements NotificationService{

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationRepository.getNotificationByUserId(userId);
    }

    @Override
    public Notification getNotificationById(int id) {
        return notificationRepository.getNotificationById(id);
    }

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }
}
