package app.backendclinic.pacientes.services;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import app.backendclinic.pacientes.Dtos.PageDto;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.repositorys.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    private final PacienteRepository pacienteRepository;
    private final BitacoraService bitacoraService;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, BitacoraService bitacoraService) {
        this.pacienteRepository = pacienteRepository;
        this.bitacoraService = bitacoraService;
    }

    public Paciente findByEmail(String email) {
        return pacienteRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public Paciente updatePaciente(String id, Paciente pacienteDetails, User usuario, HttpServletRequest request) {
        try {
            Paciente paciente = pacienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            paciente.setNombre(pacienteDetails.getNombre());
            paciente.setApellido(pacienteDetails.getApellido());
            paciente.setFechaNacimiento(pacienteDetails.getFechaNacimiento());
            paciente.setTelefono(pacienteDetails.getTelefono());
            paciente.setDireccion(pacienteDetails.getDireccion());
            paciente.setEmail(pacienteDetails.getEmail());

            Paciente updatedPaciente = pacienteRepository.save(paciente);

            // Registrar en la bitácora
            logger.debug("Registrando acción de actualización en la bitácora para el usuario: {}", usuario.getUsername());
            bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZACIÓN PACIENTE", request.getRemoteAddr(), "Exitoso");

            return updatedPaciente;
        } catch (Exception e) {
            logger.error("Error al actualizar paciente", e);

            // Registrar fallo en la bitácora
            logger.debug("Registrando fallo en la bitácora para la actualización del paciente con ID: {}", id);
            bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZACIÓN PACIENTE", request.getRemoteAddr(), "Fallido");

            throw new RuntimeException("No se pudo actualizar el paciente, intente nuevamente.");
        }
    }

    @Transactional(readOnly = true)
    public PageDto<Paciente> findAllPacientes(int page, int size, User usuario, HttpServletRequest request) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Paciente> pacientes = pacienteRepository.findAll(pageable);

            // Registrar en la bitácora
            logger.debug("Registrando acción de vista de todos los pacientes en la bitácora para el usuario: {}", usuario.getUsername());
            bitacoraService.registrarAccion("VISTA", usuario, "VISTA TODOS LOS PACIENTES", request.getRemoteAddr(), "Exitoso");

            // Convertir el Page en PageDto
            return new PageDto<>(pacientes);
        } catch (Exception e) {
            logger.error("Error al obtener lista de pacientes", e);

            // Registrar fallo en la bitácora
            bitacoraService.registrarAccion("VISTA", usuario, "VISTA TODOS LOS PACIENTES", request.getRemoteAddr(), "Fallido");

            throw new RuntimeException("No se pudo obtener la lista de pacientes.");
        }
    }

    @Transactional(readOnly = true)
    public Paciente findPacienteById(String id) {
        try {
            return pacienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        } catch (Exception e) {
            logger.error("Error al obtener paciente por ID", e);
            throw new RuntimeException("No se pudo obtener el paciente.");
        }
    }
}
