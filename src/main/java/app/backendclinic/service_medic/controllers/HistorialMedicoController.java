package app.backendclinic.service_medic.controllers;

import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.services.HistorialMedicoService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<HistorialMedico> getHistorialMedico(@PathVariable String pacienteId) {
        HistorialMedico historial = historialMedicoService.getHistorialMedico(pacienteId);
        return historial != null ? new ResponseEntity<>(historial, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
