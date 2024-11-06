package app.backendclinic.user.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.user.models.Permiso;



@Repository
public interface PermisoRepository extends JpaRepository<Permiso, String>{
}