package fpt.aptech.trackmentalhealth.repository.user;

import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT e FROM Users e")
    List<Users> findAll();

    @Query("SELECT e FROM Users e WHERE e.id = :id")
    Users findById(String id);
}
