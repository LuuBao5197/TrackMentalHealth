package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.service.Notification.NotificationService;
import fpt.aptech.trackmentalhealth.service.appointment.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AppointmentService appointmentService;

    // Lấy danh sách notification theo user
    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUserId(@PathVariable int userId) {
        return notificationService.getNotificationsByUserId(userId);
    }

    // Lấy 1 notification
    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable int id) {
        return notificationService.getNotificationById(id);
    }

    // Tạo notification thủ công (API)
    @PostMapping("/save")
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.createNotification(notification);
    }

    // Xóa notification
    @DeleteMapping("/delete/{id}")
    public void deleteNotification(@PathVariable int id) {
        notificationService.deleteNotification(id);
    }

    // Đổi trạng thái read/unread
    @PutMapping("/changestatus/{id}")
    public void changeStatus(@PathVariable int id) {
        notificationService.changeStatus(id);
    }


    @Scheduled(cron = "0 * * * * ?")
    public void sendReminderForAppointments() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        logger.info("⏰ Checking appointments at: {}", now);

        List<Appointment> appointments = appointmentService.getApprovedAppointmentsByDateTime(now);

        if (appointments.isEmpty()) {
            logger.info("No appointments found at {}", now);
        } else {
            for (Appointment appt : appointments) {
                String message = "Reminder: You have an appointment now at " + appt.getTimeStart() + ".";
                logger.info("Sending reminder to user {}: {}", appt.getUser().getId(), message);
                notificationService.createNotification(appt.getUser(), message);
            }
        }
    }


}
