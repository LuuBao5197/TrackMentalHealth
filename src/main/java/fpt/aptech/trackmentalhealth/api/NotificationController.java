package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Notification;
import fpt.aptech.trackmentalhealth.service.Notification.NotificationService;
import fpt.aptech.trackmentalhealth.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // Scheduler gửi nhắc lịch mỗi 8h sáng
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReminderForTomorrowAppointments() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // Lấy appointment APPROVED của ngày mai
        List<Appointment> appointments = appointmentService.getApprovedAppointmentsByDate(tomorrow);

        for (Appointment appt : appointments) {
            String message = "Reminder: Tomorrow you have an appointment at " + appt.getTimeStart() + ".";
            notificationService.createNotification(appt.getUser(), message);
        }
    }
}
