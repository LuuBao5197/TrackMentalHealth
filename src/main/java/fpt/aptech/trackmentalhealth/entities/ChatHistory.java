package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "chat_history")
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    private String role;
    @Column(name = "message", length = 255, columnDefinition = "nvarchar(255)")
    private String message;
    private LocalDateTime timestamp;

}
