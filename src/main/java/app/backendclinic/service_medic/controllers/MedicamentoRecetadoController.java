package app.backendclinic.service_medic.controllers;

import app.backendclinic.service_medic.Dtos.MedicamentoRecetadoDto;
import app.backendclinic.service_medic.models.AnotacionHistorial;
import app.backendclinic.service_medic.models.MedicamentoRecetado;
import app.backendclinic.service_medic.services.MedicamentoRecetadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medicamentos-recetados")
@RequiredArgsConstructor
public class MedicamentoRecetadoController {

    private final MedicamentoRecetadoService medicamentoRecetadoService;

    @PostMapping
    public ResponseEntity<?> agregarMedicamento(@RequestBody MedicamentoRecetadoDto medicamentoDto) {
        try {
            // Transformar DTO a entidad MedicamentoRecetado
            MedicamentoRecetado medicamento = MedicamentoRecetado.builder()
                    .anotacionHistorial(AnotacionHistorial.builder()
                            .id(medicamentoDto.getAnotacionHistorialId())
                            .build())
                    .nombre(medicamentoDto.getNombre())
                    .dosis(medicamentoDto.getDosis())
                    .unidadDosis(medicamentoDto.getUnidadDosis())
                    .frecuenciaHoras(medicamentoDto.getFrecuenciaHoras())
                    .duracionDias(medicamentoDto.getDuracionDias())
                    .horaInicio(medicamentoDto.getHoraInicio())
                    .build();

            MedicamentoRecetado nuevoMedicamento = medicamentoRecetadoService.agregarMedicamento(medicamento);
            return ResponseEntity.ok(nuevoMedicamento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{anotacionHistorialId}")
    public ResponseEntity<?> listarMedicamentos(@PathVariable String anotacionHistorialId) {
        try {
            List<MedicamentoRecetado> medicamentos = medicamentoRecetadoService.listarMedicamentosPorHistorial(anotacionHistorialId);
            return ResponseEntity.ok(medicamentos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/medicamento/{id}")
    public ResponseEntity<?> obtenerMedicamento(@PathVariable String id) {
        try {
            MedicamentoRecetado medicamento = medicamentoRecetadoService.obtenerMedicamentoPorId(id);
            return ResponseEntity.ok(medicamento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/hora-inicio")
    public ResponseEntity<?> actualizarHoraInicio(@PathVariable String id, @RequestBody LocalDateTime horaInicio) {
        try {
            MedicamentoRecetado actualizado = medicamentoRecetadoService.actualizarHoraInicio(id, horaInicio);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoMedicamento(@PathVariable String id, @RequestBody boolean completado) {
        try {
            MedicamentoRecetado medicamentoActualizado = medicamentoRecetadoService.actualizarEstadoMedicamento(id, completado);
            return ResponseEntity.ok(medicamentoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedicamento(@PathVariable String id) {
        try {
            medicamentoRecetadoService.eliminarMedicamento(id);
            return ResponseEntity.ok("Medicamento eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
