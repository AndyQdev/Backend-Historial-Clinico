package app.backendclinic.service_medic.controllers;

import app.backendclinic.service_medic.dtos.CreateDatoEnfermeriaRequest;
import app.backendclinic.service_medic.models.DatoEnfermeria;
import app.backendclinic.service_medic.services.DatoEnfermeriaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/datos-enfermeria")
public class DatoEnfermeriaController {

    private final DatoEnfermeriaService datoEnfermeriaService;

    public DatoEnfermeriaController(DatoEnfermeriaService datoEnfermeriaService) {
        this.datoEnfermeriaService = datoEnfermeriaService;
    }

    @PostMapping("/{historialMedicoId}")
    public ResponseEntity<DatoEnfermeria> addDatoEnfermeria(@PathVariable String historialMedicoId,
                                                            @RequestBody CreateDatoEnfermeriaRequest request) {
        DatoEnfermeria nuevoDato = datoEnfermeriaService.addDatoEnfermeria(
                historialMedicoId,
                request.getPeso(),
                request.getEstatura(),
                request.getPresion(),
                request.getTemperatura(),
                request.getSaturacion(),
                request.getFrecuenciaRespiratoria(),
                request.getFrecuenciaCardiaca(),
                request.getFechaRegistro()
        );
        return new ResponseEntity<>(nuevoDato, HttpStatus.CREATED);
    }
}
