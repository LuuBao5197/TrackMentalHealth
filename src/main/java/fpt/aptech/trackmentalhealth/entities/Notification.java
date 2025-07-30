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

    @Column(columnDefinition = "NVARCHAR(50)")
    private String type;

    private boolean isRead;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(name = "description", columnDefinition = "NVARCHAR(500)")
    private String des;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String message;

    private LocalDateTime datetime;

    public Notification() {}

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
