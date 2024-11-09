package app.backendclinic.service_medic.controllers;

import app.backendclinic.service_medic.Dtos.CreateAntecedenteRequest;
import app.backendclinic.service_medic.models.Antecedente;
import app.backendclinic.service_medic.services.AntecedenteService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api/antecedentes")
public class AntecedenteController {

    private final AntecedenteService antecedenteService;

    public AntecedenteController(AntecedenteService antecedenteService) {
        this.antecedenteService = antecedenteService;
    }

    @PostMapping("/create/{historialMedicoId}")
    public ResponseEntity<Antecedente> createAntecedente(
            @PathVariable String historialMedicoId,
            @RequestBody CreateAntecedenteRequest request) {

        // Crear un nuevo antecedente usando los datos del DTO
        Antecedente nuevoAntecedente = antecedenteService.addAntecedente(
                historialMedicoId,
                request.getDescripcion(),
                request.getFechaRegistro());

        return new ResponseEntity<>(nuevoAntecedente, HttpStatus.CREATED);
    }

    @GetMapping("/get/{historialMedicoId}")
    public ResponseEntity<List<Antecedente>> getAntecedentesByHistorialMedico(@PathVariable String historialMedicoId) {
        List<Antecedente> antecedentes = antecedenteService.getAntecedentesByHistorialMedico(historialMedicoId);
        return new ResponseEntity<>(antecedentes, HttpStatus.OK);
    }

    @PutMapping("/edit/{antecedenteId}")
    public ResponseEntity<Antecedente> updateAntecedente(@PathVariable String antecedenteId, @RequestBody Antecedente antecedente) {
        Antecedente updatedAntecedente = antecedenteService.updateAntecedente(antecedenteId, antecedente);
        return new ResponseEntity<>(updatedAntecedente, HttpStatus.OK);
    }
}
