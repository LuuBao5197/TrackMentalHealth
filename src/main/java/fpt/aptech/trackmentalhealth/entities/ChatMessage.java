package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@Getter
@Setter
@Entity
@Table(name = "ChatMessages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private fpt.aptech.trackmentalhealth.entities.ChatSession session;

    @Column(name = "sender_id")
    private Integer senderId;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private Boolean isRead;

}