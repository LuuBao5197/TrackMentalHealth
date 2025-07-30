package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUserId(@PathVariable int userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable int id) {
        return notificationService.getNotificationById(id);
    }

    @PostMapping("/save")
    public Notification saveNotification(@RequestBody Notification notification) {
        Notification saved = notificationService.createNotification(notification);

        // Gửi tới client có subscribe topic này
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + saved.getUser().getId(), // gửi riêng cho user
                saved
        );
        return saved;
    }


    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable int id) {
        Notification notification = notificationService.getNotificationById(id);
        if(notification != null) {
            notificationService.deleteNotification(id);
        }
        else {
            throw new RuntimeException("Notification not found");
        }
    }

    @PutMapping("/changestatus/{id}")
    public void changeStatus(@PathVariable int id){
        notificationService.changeStatus(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotificationById(@PathVariable int id){
        notificationService.deleteNotification(id);
    }
}
