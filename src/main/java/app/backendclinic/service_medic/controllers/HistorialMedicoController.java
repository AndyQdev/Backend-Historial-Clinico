package app.backendclinic.service_medic.controllers;

import app.backendclinic.models.CustomUserDetails;
import app.backendclinic.models.User;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.services.HistorialMedicoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/historial-medico")
public class HistorialMedicoController {

    private final HistorialMedicoService historialMedicoService;

    public HistorialMedicoController(HistorialMedicoService historialMedicoService) {
        this.historialMedicoService = historialMedicoService;
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<HistorialMedico> getHistorialMedico(@PathVariable String pacienteId, HttpServletRequest request) {
        try {
            // Verificar autenticación
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Obtener el historial médico asociado al paciente
            HistorialMedico historial = historialMedicoService.getHistorialMedico(pacienteId);
            return historial != null
                    ? ResponseEntity.ok(historial)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
}
}
