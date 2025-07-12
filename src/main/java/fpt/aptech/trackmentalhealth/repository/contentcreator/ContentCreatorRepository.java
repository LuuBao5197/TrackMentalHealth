package fpt.aptech.trackmentalhealth.repository.contentcreator;

import fpt.aptech.trackmentalhealth.entities.ContentCreator;
import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentCreatorRepository extends JpaRepository<ContentCreator, Integer> {
    Optional<ContentCreator> findByUser(Users user);
}
