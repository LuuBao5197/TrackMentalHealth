package fpt.aptech.trackmentalhealth.service.appointment;

import fpt.aptech.trackmentalhealth.entities.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointmentByUserId(int userId);

    List<Appointment> getAppointmentByPsyId(int psyId);

    Appointment getAppointmentById(int id);

    Appointment createAppointment(Appointment appointment);

    void updateAppointment(Appointment appointment);

    void deleteAppointment(int id);

    boolean hasPendingAppointment(int userId, int psyId);

    List<Appointment> getApprovedAppointmentsByDateTime(LocalDateTime date);

}
