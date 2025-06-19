package fpt.aptech.trackmentalhealth.api;
import fpt.aptech.trackmentalhealth.entities.Appointment;
import fpt.aptech.trackmentalhealth.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")

public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/list/{psyId}")
    public List<Appointment> getAppointmentByPsyId(@PathVariable int psyId) {
        return appointmentService.getAppointmentByPsyId(psyId);
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable int id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping("/")
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @PutMapping("/")
    public void updateAppointment(@RequestBody Appointment appointment) {
        appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
    }
}
