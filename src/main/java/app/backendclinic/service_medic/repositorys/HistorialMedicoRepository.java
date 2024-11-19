package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, String> {
    HistorialMedico findByPacienteId(String pacienteId);

    // Nuevo método: Devuelve un Optional para manejar datos ausentes de manera segura
    Optional<HistorialMedico> findOptionalByPacienteId(String pacienteId);
}
