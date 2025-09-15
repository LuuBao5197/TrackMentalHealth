package fpt.aptech.trackmentalhealth.service.appointment;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.repository.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentServiceImp implements AppointmentService{
    @Autowired
    AppointmentRepository appointmentRepository;


    @Override
    public List<Appointment> getAppointmentByUserId(int userId) {
        return appointmentRepository.getAppointmentByUserId(userId);
    }

    @Override
    public List<Appointment> getAppointmentByPsyId(int psyId) {
        return appointmentRepository.getAppointmentByPsychologistId(psyId);
    }

    @Override
    public Appointment getAppointmentById(int id) {
        return appointmentRepository.getAppointmentById(id);
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        int userId = appointment.getUser().getId();
        int psyId = appointment.getPsychologist().getId();

        if (hasPendingAppointment(userId, psyId)) {
            throw new RuntimeException("You already have a pending appointment with this psychologist.");
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public boolean hasPendingAppointment(int userId, int psyId) {
        List<Appointment> existing = appointmentRepository.findPendingAppointmentByUserAndPsychologist(userId, psyId);
        return !existing.isEmpty();
    }

    @Override
    public List<Appointment> getApprovedAppointmentsByDateTime(LocalDateTime dateTime) {
        return appointmentRepository.findApprovedAppointmentsByDateTime(dateTime);
    }



}
