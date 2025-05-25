package fpt.aptech.trackmentalhealth.repositories;

import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT e FROM Users e WHERE e.email = :email")
    Users findByEmail(@Param("email") String email);

}

