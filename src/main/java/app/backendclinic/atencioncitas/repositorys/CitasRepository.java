package app.backendclinic.atencioncitas.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.atencioncitas.models.Cita;
import java.util.List;

@Repository
public interface CitasRepository extends JpaRepository<Cita, String>{
    // Obtener citas por usuario (médico o paciente)
    List<Cita> findByMedicoUsuarioIdOrPacienteId(String medicoUsuarioId, String pacienteId);

    // Obtener citas de un paciente específico
    List<Cita> findByPacienteId(String pacienteId);
}
