package app.backendclinic.service_medic.controllers;

import app.backendclinic.models.User;
import app.backendclinic.service_medic.dtos.CreateAntecedenteRequest;
import app.backendclinic.service_medic.models.Antecedente;
import app.backendclinic.service_medic.services.AntecedenteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping("/create/{historialMedicoId}")
    public ResponseEntity<Antecedente> createAntecedente(
            @PathVariable String historialMedicoId,
            @RequestBody CreateAntecedenteRequest request,
            HttpServletRequest httpRequest) {

        User usuario = getAuthenticatedUser();
        String ipAddress = httpRequest.getRemoteAddr();

        Antecedente nuevoAntecedente = antecedenteService.addAntecedente(
                historialMedicoId,
                request.getDescripcion(),
                request.getFechaRegistro(),
                usuario,
                ipAddress
        );

        return new ResponseEntity<>(nuevoAntecedente, HttpStatus.CREATED);
    }

    @GetMapping("/get/{historialMedicoId}")
    public ResponseEntity<List<Antecedente>> getAntecedentesByHistorialMedico(@PathVariable String historialMedicoId, HttpServletRequest httpRequest) {
        User usuario = getAuthenticatedUser();
        String ipAddress = httpRequest.getRemoteAddr();

        List<Antecedente> antecedentes = antecedenteService.getAntecedentesByHistorialMedico(historialMedicoId, usuario, ipAddress);
        return new ResponseEntity<>(antecedentes, HttpStatus.OK);
    }

    @PutMapping("/edit/{antecedenteId}")
    public ResponseEntity<Antecedente> updateAntecedente(@PathVariable String antecedenteId, @RequestBody Antecedente antecedente, HttpServletRequest httpRequest) {
        User usuario = getAuthenticatedUser();
        String ipAddress = httpRequest.getRemoteAddr();

        Antecedente updatedAntecedente = antecedenteService.updateAntecedente(antecedenteId, antecedente, usuario, ipAddress);
        return new ResponseEntity<>(updatedAntecedente, HttpStatus.OK);
    }
}
