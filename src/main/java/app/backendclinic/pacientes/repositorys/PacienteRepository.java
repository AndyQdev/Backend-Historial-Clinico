package app.backendclinic.pacientes.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.backendclinic.pacientes.models.Paciente;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String>{
    Optional<Paciente> findByEmail(String email);
}
