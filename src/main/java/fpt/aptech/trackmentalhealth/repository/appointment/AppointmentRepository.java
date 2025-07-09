package fpt.aptech.trackmentalhealth.repository.appointment;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    @Query("select a from Appointment a where a.psychologist.id=:psyId")
    List<Appointment> getAppointmentByPsyId(int psyId);

    @Query("select a from Appointment a where a.id = :id")
    Appointment getAppointmentById(int id);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId AND a.psychologist.id = :psyId AND a.status = 'PENDING'")
    List<Appointment> findPendingAppointmentByUserAndPsychologist(int userId, int psyId);

}
