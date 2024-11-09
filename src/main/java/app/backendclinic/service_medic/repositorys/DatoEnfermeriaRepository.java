package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.DatoEnfermeria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DatoEnfermeriaRepository extends JpaRepository<DatoEnfermeria, String> {
}
