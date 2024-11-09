package app.backendclinic.service_medic.services;

import app.backendclinic.pacientes.repositorys.PacienteRepository;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.repositorys.HistorialMedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import app.backendclinic.pacientes.models.Paciente;

import java.util.List;
import java.util.UUID;


@Service
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final PacienteRepository pacienteRepository;

    public HistorialMedicoService(HistorialMedicoRepository historialMedicoRepository, PacienteRepository pacienteRepository) {
        this.historialMedicoRepository = historialMedicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public HistorialMedico createHistorialMedico(String pacienteId) {
        // Buscar el paciente en la base de datos
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Crear el historial médico y asociarlo al paciente
        HistorialMedico historialMedico = HistorialMedico.builder()
                .id(UUID.randomUUID().toString())
                .paciente(paciente)
                .build();

        // Guardar el historial médico en la base de datos
        return historialMedicoRepository.save(historialMedico);
    }

    // Método para crear historiales médicos faltantes
    @Transactional
    public void crearHistorialesMedicosFaltantes() {
        List<Paciente> pacientesSinHistorial = pacienteRepository.findAll().stream()
                .filter(paciente -> historialMedicoRepository.findByPacienteId(paciente.getId()) == null)
                .toList();

        for (Paciente paciente : pacientesSinHistorial) {
            createHistorialMedico(paciente.getId());
        }
    }

    @Transactional(readOnly = true)
    public HistorialMedico getHistorialMedico(String pacienteId) {
        return historialMedicoRepository.findByPacienteId(pacienteId);
    }
}
