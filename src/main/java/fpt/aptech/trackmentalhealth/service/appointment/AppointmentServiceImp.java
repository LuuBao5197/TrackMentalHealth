package fpt.aptech.trackmentalhealth.service.appointment;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.repository.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImp implements AppointmentService{
    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAppointmentByPsyId(int psyId) {
        return appointmentRepository.getAppointmentByPsyId(psyId);
    }

    @Override
    public Appointment getAppointmentById(int id) {
        return appointmentRepository.getAppointmentById(id);
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
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
}
