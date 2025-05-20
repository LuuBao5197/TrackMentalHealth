package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ChatSessions")
public class ChatSession {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private fpt.aptech.trackmentalhealth.entities.User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psychologist_code")
    private fpt.aptech.trackmentalhealth.entities.Psychologist psychologistCode;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Size(max = 255)
    @Nationalized
    @Column(name = "status")
    private String status;

}