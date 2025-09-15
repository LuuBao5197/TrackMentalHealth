package fpt.aptech.trackmentalhealth.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time_start")
    private LocalDateTime timeStart;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "userid")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "psychologistid")
    private Psychologist psychologist;

    @Column(name = "note")
    private String note;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id")
    private Review review;



    public Appointment() {
    }

    public Appointment(String status, LocalDateTime timeStart, Users user, Psychologist psychologist, String note) {
        this.status = status;
        this.timeStart = timeStart;
        this.user = user;
        this.psychologist = psychologist;
        this.note = note;
    }
}
