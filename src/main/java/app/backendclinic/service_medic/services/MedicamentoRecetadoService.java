package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.models.MedicamentoRecetado;

import app.backendclinic.service_medic.repositorys.MedicamentoRecetadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicamentoRecetadoService {

    private final MedicamentoRecetadoRepository medicamentoRecetadoRepository;

    public MedicamentoRecetado agregarMedicamento(MedicamentoRecetado medicamento) {
        try {
            return medicamentoRecetadoRepository.save(medicamento);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el medicamento: " + e.getMessage());
        }
    }

    public List<MedicamentoRecetado> listarMedicamentosPorHistorial(String historialId) {
        try {
            return medicamentoRecetadoRepository.findByAnotacionHistorialId(historialId);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar medicamentos: " + e.getMessage());
        }
    }

    public MedicamentoRecetado obtenerMedicamentoPorId(String id) {
        try {
            return medicamentoRecetadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el medicamento: " + e.getMessage());
        }
    }

    public MedicamentoRecetado actualizarHoraInicio(String id, LocalDateTime horaInicio) {
        try {
            MedicamentoRecetado medicamento = medicamentoRecetadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + id));
            medicamento.setHoraInicio(horaInicio);
            return medicamentoRecetadoRepository.save(medicamento);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la hora de inicio: " + e.getMessage());
        }
    }

    public void eliminarMedicamento(String id) {
        try {
            MedicamentoRecetado medicamento = medicamentoRecetadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + id));
            medicamentoRecetadoRepository.delete(medicamento);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el medicamento: " + e.getMessage());
        }
    }
    public MedicamentoRecetado actualizarEstadoMedicamento(String id, boolean completado) {
        try {
            MedicamentoRecetado medicamento = medicamentoRecetadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + id));

            medicamento.setCompletado(completado);
            return medicamentoRecetadoRepository.save(medicamento);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado del medicamento: " + e.getMessage());
        }
    }
}
