package fpt.aptech.trackmentalhealth.service.appointment;

import fpt.aptech.trackmentalhealth.entities.Appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointmentByPsyId(int id);

    Appointment getAppointmentById(int id);

    Appointment createAppointment(Appointment appointment);

    void updateAppointment(Appointment appointment);

    void deleteAppointment(int id);

    boolean hasPendingAppointment(int userId, int psyId);

}
