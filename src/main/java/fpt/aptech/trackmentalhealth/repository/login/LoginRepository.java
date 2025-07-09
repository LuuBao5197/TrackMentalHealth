package fpt.aptech.trackmentalhealth.repository.login;

import fpt.aptech.trackmentalhealth.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT e FROM Users e WHERE e.email = :email")
    Users findByEmail(@Param("email") String email);

    Optional<Users> findById(int id);

    List<Users> findByRoleId_Id(Integer roleIdId);
    
}