package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.AnotacionHistorial;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnotacionHistorialRepository extends JpaRepository<AnotacionHistorial, String> {
}
