package fpt.aptech.trackmentalhealth.repository.login;

import fpt.aptech.trackmentalhealth.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}
