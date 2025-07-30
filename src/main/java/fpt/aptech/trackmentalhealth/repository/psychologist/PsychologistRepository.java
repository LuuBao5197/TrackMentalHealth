package fpt.aptech.trackmentalhealth.repository.psychologist;

import fpt.aptech.trackmentalhealth.entities.Psychologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PsychologistRepository extends JpaRepository<Psychologist, Integer> {
    @Query("select p from Psychologist p")
    List<Psychologist> getPsychologists();

    @Query("select p from Psychologist p where p.id=:id")
    Psychologist getPsychologistById(int id);

    @Query("select p from Psychologist p where p.usersID.id=:id")
    Psychologist getPsychologistByUserId(int id);
}
