package fpt.aptech.trackmentalhealth.service.psychologist;

import fpt.aptech.trackmentalhealth.entities.Psychologist;
import fpt.aptech.trackmentalhealth.repository.psychologist.PsychologistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PsychologistServiceImp implements PsychologistService{
    @Autowired
    PsychologistRepository psychologistRepository;

    @Override
    public List<Psychologist> getAllPsychologists() {
        return psychologistRepository.getPsychologists();
    }

    @Override
    public Psychologist getPsyById(int id) {
        return psychologistRepository.getPsychologistById(id);
    }

    @Override
    public Psychologist getPsychologistByUserId(int id) {
        return psychologistRepository.getPsychologistByUserId(id);
    }



}
