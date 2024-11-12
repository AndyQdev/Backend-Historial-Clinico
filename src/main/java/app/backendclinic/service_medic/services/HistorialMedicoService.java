package app.backendclinic.service_medic.services;

import app.backendclinic.Reportes.models.Bitacora;
import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import app.backendclinic.pacientes.repositorys.PacienteRepository;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.repositorys.HistorialMedicoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import app.backendclinic.pacientes.models.Paciente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final PacienteRepository pacienteRepository;
    private final BitacoraService bitacoraService;

    public HistorialMedicoService(HistorialMedicoRepository historialMedicoRepository, PacienteRepository pacienteRepository, BitacoraService bitacoraService) {
        this.historialMedicoRepository = historialMedicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.bitacoraService = bitacoraService;
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
    public void crearHistorialesMedicosFaltantes(User usuario, HttpServletRequest request) {
        List<Paciente> pacientesSinHistorial = pacienteRepository.findAll().stream()
                .filter(paciente -> historialMedicoRepository.findByPacienteId(paciente.getId()) == null)
                .toList();

        for (Paciente paciente : pacientesSinHistorial) {
            try {
                createHistorialMedico(paciente.getId());

                // Registrar en la bitácora cada creación exitosa
                registrarAccionBitacora("CREACIÓN", usuario, "CREACIÓN AUTOMÁTICA HISTORIAL MÉDICO", request.getRemoteAddr(), "Exitoso");
            } catch (Exception e) {
                // Registrar en la bitácora en caso de fallo
                registrarAccionBitacora("CREACIÓN", usuario, "CREACIÓN AUTOMÁTICA HISTORIAL MÉDICO", request.getRemoteAddr(), "Fallido");
            }
        }
    }

    @Transactional(readOnly = true)
    public HistorialMedico getHistorialMedico(String pacienteId) {
        return historialMedicoRepository.findByPacienteId(pacienteId);
    }
    public void registrarAccionBitacora(String tipoAccion, User usuario, String accion, String ipAddress, String resultado) {
        bitacoraService.registrarAccion(tipoAccion, usuario, accion, ipAddress, resultado);
    }
}
