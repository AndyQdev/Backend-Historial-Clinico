package app.backendclinic.service_medic.controllers;
import app.backendclinic.service_medic.models.AnotacionHistorial;
import app.backendclinic.service_medic.services.AnotacionHistorialService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/anotaciones-historial")
public class AnotacionHistorialController {

    private final AnotacionHistorialService anotacionHistorialService;

    public AnotacionHistorialController(AnotacionHistorialService anotacionHistorialService) {
        this.anotacionHistorialService = anotacionHistorialService;
    }

    @PostMapping("/{historialMedicoId}/{medicoId}")
    public ResponseEntity<AnotacionHistorial> addAnotacion(@PathVariable String historialMedicoId,
                                                           @PathVariable String medicoId,
                                                           @RequestBody AnotacionRequest request) {
        AnotacionHistorial nuevaAnotacion = anotacionHistorialService.addAnotacion(
                historialMedicoId, medicoId, request.getDescripcion(), request.getTratamiento());
        return new ResponseEntity<>(nuevaAnotacion, HttpStatus.CREATED);
    }

    // Request DTO para recibir `descripcion` y `tratamiento`
    public static class AnotacionRequest {
        private String descripcion;
        private String tratamiento;

        // Getters y Setters
        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getTratamiento() {
            return tratamiento;
        }

        public void setTratamiento(String tratamiento) {
            this.tratamiento = tratamiento;
        }
    }
}
