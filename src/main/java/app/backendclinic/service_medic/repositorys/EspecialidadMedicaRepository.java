package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.EspecialidadMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadMedicaRepository extends JpaRepository<EspecialidadMedica, String> {
}
