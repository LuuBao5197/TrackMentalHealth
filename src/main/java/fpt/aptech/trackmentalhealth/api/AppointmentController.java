package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.entities.Psychologist;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.psychologist.PsychologistRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/list/{userId}")
    public List<Appointment> getAppointmentByUserId(@PathVariable int userId) {
        List<Appointment> appointments = appointmentService.getAppointmentByUserId(userId);
        return appointments;
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable int id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping("/save")
    public Appointment saveAppointment(@RequestBody Appointment appointment) {
        // Fetch user by id
        Users user = userRepository.findById(appointment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch psychologist by id
        Psychologist psy = psychologistRepository.getPsychologistById(appointment.getPsychologist().getId());
        if (psy == null) {
            throw new RuntimeException("Psychologist not found");
        }

        appointment.setUser(user);
        appointment.setPsychologist(psy);

        boolean hasPending = appointmentService.hasPendingAppointment(user.getId(), psy.getId());
        if (hasPending) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Đã có lịch hẹn đang chờ xử lý với chuyên gia này.");
        }
        return appointmentService.createAppointment(appointment);
    }


    @PutMapping("/{id}")
    public void updateAppointment(@PathVariable int id, @RequestBody Appointment appointment) {
        if (appointment.getId() != id) {
            throw new IllegalArgumentException("ID mismatch");
        }
        appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
    }

    @GetMapping("/psychologist/{userId}")
    public List<Appointment> getAppointmentsByPsychologistId(@PathVariable int userId){
        Psychologist psy = psychologistRepository.getPsychologistByUserId(userId);
        return appointmentService.getAppointmentByPsyId(psy.getId());
    }

}
