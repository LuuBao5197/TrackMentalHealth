package fpt.aptech.trackmentalhealth.repository.appointment;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("select a from Appointment a where a.user.id=:userId")
    List<Appointment> getAppointmentByUserId(int userId);

    @Query("select a from Appointment a where a.id = :id")
    Appointment getAppointmentById(int id);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId AND a.psychologist.id = :psyId AND a.status = 'PENDING'")
    List<Appointment> findPendingAppointmentByUserAndPsychologist(int userId, int psyId);

    @Query("select a from Appointment a where a.psychologist.id=:psyId")
    List<Appointment> getAppointmentByPsychologistId(int psyId);

    @Query("SELECT a FROM Appointment a WHERE a.status = 'APPROVED' AND a.timeStart = :date")
    List<Appointment> findApprovedAppointmentsByDate(@Param("date") LocalDate date);

}
