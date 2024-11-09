package app.backendclinic.pacientes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.services.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Endpoint para actualizar un paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable String id, @RequestBody Paciente pacienteDetails) {
        try {
            Paciente updatedPaciente = pacienteService.updatePaciente(id, pacienteDetails);
            return new ResponseEntity<>(updatedPaciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // o manejar con mensaje de error personalizado
        }
    }

    // Endpoint para listar todos los pacientes con paginación
    @GetMapping
    public ResponseEntity<Page<Paciente>> getAllPacientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Paciente> pacientes = pacienteService.findAllPacientes(page, size);
            return new ResponseEntity<>(pacientes, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // manejar con mensaje de error personalizado
        }
    }

    // Endpoint para obtener un paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable String id) {
        try {
            Paciente paciente = pacienteService.findPacienteById(id);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // o manejar con mensaje de error personalizado
        }
    }
}

