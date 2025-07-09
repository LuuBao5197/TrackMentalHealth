package fpt.aptech.trackmentalhealth.api;


import fpt.aptech.trackmentalhealth.entities.Psychologist;
import fpt.aptech.trackmentalhealth.service.psychologist.PsychologistService;
import jakarta.validation.constraints.Digits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/psychologist")
@CrossOrigin(origins = "http://localhost:3000")
public class PsychologistController {
    @Autowired
    PsychologistService psychologistService;

    @GetMapping("/")
    public List<Psychologist> getPsychologists() {
        return psychologistService.getAllPsychologists();
    }

    @GetMapping("/{id}")
    public Psychologist getPsychologist(@PathVariable int id) {
        return psychologistService.getPsyById(id);
    }
}
