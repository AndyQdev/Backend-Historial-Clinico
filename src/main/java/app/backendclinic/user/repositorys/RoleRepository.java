package app.backendclinic.user.repositorys;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.user.models.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{
    Optional<Role> findByName(String name);
}
