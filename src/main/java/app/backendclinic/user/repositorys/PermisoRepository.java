package app.backendclinic.user.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.user.models.Permiso;

import java.util.List;


@Repository
public interface PermisoRepository extends JpaRepository<Permiso, String> {
    // Método para verificar si un permiso ya existe por su nombre
    boolean existsByName(String name);
    List<Permiso> findAllByNameIn(List<String> names);
}
