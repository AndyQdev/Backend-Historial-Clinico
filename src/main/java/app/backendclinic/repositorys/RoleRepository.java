package app.backendclinic.repositorys;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.backendclinic.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{
}
