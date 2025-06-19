package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {

    // mquan them 1 entities quan ly lich hen cua Psycho va user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "time_start")
    private LocalDateTime timeStart ;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "psychologist")
    private Psychologist psychologist;

    @Column(name = "note")
    private String note;

    public Appointment() {
    }

    public Appointment(String status, LocalDateTime timeStart, Users user, Psychologist psychologist, String note) {
        this.status = status;
        this.timeStart = timeStart;
        this.user= user;
        this.psychologist = psychologist;
        this.note = note;
    }
}


