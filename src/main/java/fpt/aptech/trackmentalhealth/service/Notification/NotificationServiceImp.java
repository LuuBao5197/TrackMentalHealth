package fpt.aptech.trackmentalhealth.service.Notification;

import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public List<Notification> getNotificationsByUserId(int userId) {
        // Đảm bảo repository có method findByUser_Id
        return notificationRepository.getNotificationByUserId(userId);
    }

    @Override
    public Notification getNotificationById(int id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Override
    public Notification createNotification(Notification notification) {
        Notification saved = notificationRepository.save(notification);

        // Gửi realtime qua WebSocket
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + saved.getUser().getId(),
                saved
        );

        return saved;
    }

    @Override
    public Notification createNotification(Users user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setRead(false);

        return createNotification(notification); // tái sử dụng method trên
    }

    @Override
    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void changeStatus(int id) {
        Notification notification = getNotificationById(id);
        notification.setRead(!notification.isRead());
        notificationRepository.save(notification);
    }
}
