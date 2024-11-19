package app.backendclinic.atencioncitas.controllers;

import app.backendclinic.atencioncitas.models.EstadoCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.backendclinic.atencioncitas.dtos.CrearCitaRequestDTO;
import app.backendclinic.atencioncitas.models.Cita;
import app.backendclinic.atencioncitas.services.CitaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> obtenerTodasLasCitas() {
        List<Cita> citas = citaService.obtenerTodasLasCitas();
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable String id) {
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Cita> crearCita(@RequestBody CrearCitaRequestDTO crearCitaRequest) {
        Cita nuevaCita = citaService.crearCita(crearCitaRequest);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(
            @PathVariable String id,
            @RequestBody Cita citaActualizada) {
        try {
            Cita cita = citaService.actualizarCita(id, citaActualizada);
            return new ResponseEntity<>(cita, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable String id) {
        try {
            citaService.eliminarCita(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cita>> obtenerCitasPorUsuario(@PathVariable String usuarioId) {
        List<Cita> citas = citaService.obtenerCitasPorUsuario(usuarioId);
        if (citas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> obtenerCitasPorPaciente(@PathVariable String pacienteId) {
        List<Cita> citas = citaService.obtenerCitasPorPaciente(pacienteId);
        if (citas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstadoCita(
            @PathVariable String id,
            @RequestParam String estado) {
        try {
            EstadoCita nuevoEstado = EstadoCita.valueOf(estado.toUpperCase());
            Cita citaActualizada = citaService.actualizarEstadoCita(id, nuevoEstado);
            return new ResponseEntity<>(citaActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el estado no es válido
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Si la transición no está permitida
        }
    }

    @PatchMapping("/{id}/no_asistio")
    public ResponseEntity<Cita> marcarNoAsistio(@PathVariable String id) {
        try {
            Cita citaNoAsistio = citaService.actualizarEstadoCita(id, EstadoCita.NO_ASISTIO);
            return new ResponseEntity<>(citaNoAsistio, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Transición no permitida
        }
    }
}