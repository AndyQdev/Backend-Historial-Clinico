package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, String> {
    HistorialMedico findByPacienteId(String pacienteId);
}
