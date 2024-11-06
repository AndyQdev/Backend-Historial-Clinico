package app.backendclinic.user.repositorys;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.user.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{
}
