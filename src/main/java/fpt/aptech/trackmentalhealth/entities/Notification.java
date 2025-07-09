package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    private String type;
    private boolean isRead;
    private String title;

    @Column(name = "description")
    private String des;

    private String message;

    private LocalDateTime datetime;

    public Notification() {
    }

    public Notification(Users user, String type, boolean isRead, String title, String des, String message, LocalDateTime datetime) {
        this.user = user;
        this.type = type;
        this.isRead = isRead;
        this.title = title;
        this.des = des;
        this.message = message;
        this.datetime = datetime;
    }

}
