package app.backendclinic.atencioncitas.repositorys;

import app.backendclinic.atencioncitas.models.AtencionEspecialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtencionEspecialidadRepository extends JpaRepository<AtencionEspecialidad, String> {
}
