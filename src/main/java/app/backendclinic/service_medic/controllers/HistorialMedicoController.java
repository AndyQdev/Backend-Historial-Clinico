package app.backendclinic.service_medic.controllers;

import app.backendclinic.models.User;
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
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User usuario = (User) auth.getPrincipal();
        String ipAddress = request.getRemoteAddr();

        // Intentar obtener el historial médico
        try {
            HistorialMedico historial = historialMedicoService.getHistorialMedico(pacienteId);

            // Registrar acción en la bitácora en caso de éxito
            historialMedicoService.registrarAccionBitacora("VISTA", usuario, "CONSULTA HISTORIAL MÉDICO", ipAddress, "Exitoso");

            return historial != null ? new ResponseEntity<>(historial, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Registrar en la bitácora en caso de fallo
            historialMedicoService.registrarAccionBitacora("VISTA", usuario, "CONSULTA HISTORIAL MÉDICO", ipAddress, "Fallido");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
