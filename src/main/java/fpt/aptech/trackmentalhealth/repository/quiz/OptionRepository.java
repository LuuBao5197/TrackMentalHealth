package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {}

