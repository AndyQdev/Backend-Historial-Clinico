package app.backendclinic.pacientes.controllers;
import app.backendclinic.models.User;
import app.backendclinic.pacientes.Dtos.PageDto;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.services.PacienteService;
import app.backendclinic.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;
    private final UserRepository userRepository;

    @Autowired
    public PacienteController(PacienteService pacienteService, UserRepository userRepository) {
        this.pacienteService = pacienteService;
        this.userRepository = userRepository;
    }

    // Endpoint para actualizar un paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(
            @PathVariable String id,
            @RequestBody Paciente pacienteDetails,
            HttpServletRequest request,
            Principal principal) {
        try {
            User usuario = obtenerUsuarioDesdePrincipal(principal);
            Paciente updatedPaciente = pacienteService.updatePaciente(id, pacienteDetails, usuario, request);
            return new ResponseEntity<>(updatedPaciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para listar todos los pacientes con paginación y registrar en bitácora
    @GetMapping(produces = "application/json")
    public ResponseEntity<PageDto<Paciente>> getAllPacientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request,
            Principal principal) {
        try {
            User usuario = obtenerUsuarioDesdePrincipal(principal);
            PageDto<Paciente> pacientesDto = pacienteService.findAllPacientes(page, size, usuario, request);
            return new ResponseEntity<>(pacientesDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint para obtener un paciente por ID (sin bitácora)
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable String id) {
        try {
            Paciente paciente = pacienteService.findPacienteById(id);
            return new ResponseEntity<>(paciente, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Método para obtener el usuario actual desde el Principal usando UserRepository
    private User obtenerUsuarioDesdePrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + principal.getName()));
    }
}


