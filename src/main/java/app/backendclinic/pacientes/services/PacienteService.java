package app.backendclinic.pacientes.services;

import org.springframework.stereotype.Service;

import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.repositorys.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente findByEmail(String email) {
        return pacienteRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public Paciente updatePaciente(String id, Paciente pacienteDetails) {
        try {
            Paciente paciente = pacienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            paciente.setNombre(pacienteDetails.getNombre());
            paciente.setApellido(pacienteDetails.getApellido());
            paciente.setFechaNacimiento(pacienteDetails.getFechaNacimiento());
            paciente.setTelefono(pacienteDetails.getTelefono());
            paciente.setDireccion(pacienteDetails.getDireccion());
            paciente.setEmail(pacienteDetails.getEmail());

            return pacienteRepository.save(paciente);
        } catch (Exception e) {
            logger.error("Error al actualizar paciente", e);
            throw new RuntimeException("No se pudo actualizar el paciente, intente nuevamente.");
        }
    }

    @Transactional(readOnly = true)
    public Page<Paciente> findAllPacientes(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return pacienteRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Error al obtener lista de pacientes", e);
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

