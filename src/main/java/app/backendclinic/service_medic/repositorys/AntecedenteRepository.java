package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.Antecedente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AntecedenteRepository extends JpaRepository<Antecedente, String> {
    List<Antecedente> findByHistorialMedicoId(String historialMedicoId);
}
