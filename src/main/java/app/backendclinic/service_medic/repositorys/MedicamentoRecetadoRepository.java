package app.backendclinic.service_medic.repositorys;

import app.backendclinic.service_medic.models.MedicamentoRecetado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRecetadoRepository extends JpaRepository<MedicamentoRecetado, String> {
    List<MedicamentoRecetado> findByAnotacionHistorialId(String anotacionHistorialId);
}

