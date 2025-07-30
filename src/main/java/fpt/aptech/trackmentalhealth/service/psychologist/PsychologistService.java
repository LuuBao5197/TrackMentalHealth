package fpt.aptech.trackmentalhealth.service.psychologist;

import fpt.aptech.trackmentalhealth.entities.Psychologist;

import java.util.List;

public interface PsychologistService {
    List<Psychologist> getAllPsychologists();

    Psychologist getPsyById(int id);

    Psychologist getPsychologistByUserId(int id);
}
