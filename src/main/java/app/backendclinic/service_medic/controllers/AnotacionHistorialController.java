package app.backendclinic.service_medic.controllers;
import app.backendclinic.models.User;
import app.backendclinic.service_medic.models.AnotacionHistorial;
import app.backendclinic.service_medic.services.AnotacionHistorialService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping("/{historialMedicoId}/{medicoId}")
    public ResponseEntity<AnotacionHistorial> addAnotacion(@PathVariable String historialMedicoId,
                                                           @PathVariable String medicoId,
                                                           @RequestBody AnotacionRequest request,
                                                           HttpServletRequest httpRequest) {
        User usuario = getAuthenticatedUser();
        String ipAddress = httpRequest.getRemoteAddr();

        AnotacionHistorial nuevaAnotacion = anotacionHistorialService.addAnotacion(
                historialMedicoId,
                medicoId,
                request.getDescripcion(),
                request.getTratamiento(),
                usuario,
                ipAddress
        );
        return new ResponseEntity<>(nuevaAnotacion, HttpStatus.CREATED);
    }

    public static class AnotacionRequest {
        private String descripcion;
        private String tratamiento;

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
