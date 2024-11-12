package app.backendclinic.service_medic.controllers;

import app.backendclinic.models.User;
import app.backendclinic.service_medic.dtos.CreateDatoEnfermeriaRequest;
import app.backendclinic.service_medic.models.DatoEnfermeria;
import app.backendclinic.service_medic.services.DatoEnfermeriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping("/{historialMedicoId}")
    public ResponseEntity<DatoEnfermeria> addDatoEnfermeria(@PathVariable String historialMedicoId,
                                                            @RequestBody CreateDatoEnfermeriaRequest request,
                                                            HttpServletRequest httpRequest) {
        User usuario = getAuthenticatedUser();
        String ipAddress = httpRequest.getRemoteAddr();

        DatoEnfermeria nuevoDato = datoEnfermeriaService.addDatoEnfermeria(
                historialMedicoId,
                request.getPeso(),
                request.getEstatura(),
                request.getPresion(),
                request.getTemperatura(),
                request.getSaturacion(),
                request.getFrecuenciaRespiratoria(),
                request.getFrecuenciaCardiaca(),
                request.getFechaRegistro(),
                usuario,
                ipAddress
        );
        return new ResponseEntity<>(nuevoDato, HttpStatus.CREATED);
    }
}
