package fpt.aptech.trackmentalhealth.repository.user;

import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

}
