package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pending_user_registration")
@Getter
@Setter
public class PendingUserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pendingId;

    private String email;
    private String password;
    private String fullName;
    private Integer roleId;
    private String avatar;

    @Lob
    private byte[] certificate1;
    @Lob
    private byte[] certificate2;
    @Lob
    private byte[] certificate3;
    @Lob
    private byte[] certificate4;
    @Lob
    private byte[] certificate5;

    private LocalDateTime submittedAt;

    private Boolean isReviewed = false;
    private Boolean isApproved = false;
}
