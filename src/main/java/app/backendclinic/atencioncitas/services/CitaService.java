package app.backendclinic.atencioncitas.services;


import app.backendclinic.atencioncitas.dtos.CrearCitaRequestDTO;
import app.backendclinic.atencioncitas.models.Cita;
import app.backendclinic.atencioncitas.models.EstadoCita;
import app.backendclinic.atencioncitas.repositorys.CitasRepository;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.repositorys.PacienteRepository;
import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.models.Medico;
import app.backendclinic.service_medic.models.ServiceMedico;
import app.backendclinic.service_medic.repositorys.EspecialidadMedicaRepository;
import app.backendclinic.service_medic.repositorys.MedicoRepository;
import app.backendclinic.service_medic.repositorys.ServicioMedicoRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitasRepository citaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EspecialidadMedicaRepository especialidadMedicaRepository;
    
    @Autowired
    private ServicioMedicoRepository serviceMedicoRepository;

    // Obtener todas las citas
    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    // Obtener una cita por ID
    public Optional<Cita> obtenerCitaPorId(String id) {
        return citaRepository.findById(id);
    }

    // Crear una nueva cita
    public Cita crearCita(CrearCitaRequestDTO crearCitaRequest) {
        String medicoId = crearCitaRequest.getMedicoId();
        String pacienteId = crearCitaRequest.getPacienteId();
        String servicioId = crearCitaRequest.getServicioId();
        String especialidadId = crearCitaRequest.getEspecialidadId();

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado"));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));
        ServiceMedico servicio = serviceMedicoRepository.findById(servicioId)
                .orElseThrow(() -> new EntityNotFoundException("Servicio médico no encontrado"));
        EspecialidadMedica especialidad = especialidadMedicaRepository.findById(especialidadId)
                .orElseThrow(() -> new EntityNotFoundException("Especialidad médica no encontrada"));

        Cita nuevaCita = new Cita();
        nuevaCita.setMedico(medico);
        nuevaCita.setPaciente(paciente);
        nuevaCita.setServicioMedico(servicio);
        nuevaCita.setFechaCreada(LocalDateTime.now());
        nuevaCita.setEspecialidad(especialidad);

        if (crearCitaRequest.getEstado() != null) {
            nuevaCita.setEstado(EstadoCita.valueOf(crearCitaRequest.getEstado().toUpperCase()));
        } else {
            nuevaCita.setEstado(EstadoCita.PENDIENTE);
        }

        return citaRepository.save(nuevaCita);
    }

    // Actualizar datos generales de una cita
    public Cita actualizarCita(String id, Cita citaActualizada) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));

        if (citaActualizada.getMedico() != null) {
            cita.setMedico(citaActualizada.getMedico());
        }
        if (citaActualizada.getPaciente() != null) {
            cita.setPaciente(citaActualizada.getPaciente());
        }
        if (citaActualizada.getServicioMedico() != null) {
            cita.setServicioMedico(citaActualizada.getServicioMedico());
        }
        if (citaActualizada.getEspecialidad() != null) {
            cita.setEspecialidad(citaActualizada.getEspecialidad());
        }
        return citaRepository.save(cita);
    }

    // Actualizar estado de una cita
    public Cita actualizarEstadoCita(String id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));

        // Validar transiciones de estado
        if (cita.getEstado() == EstadoCita.CANCELADA && nuevoEstado == EstadoCita.COMPLETADA) {
            throw new IllegalStateException("No se puede completar una cita cancelada");
        }

        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new IllegalStateException("No se puede modificar una cita ya completada");
        }

        if (cita.getEstado() == EstadoCita.NO_ASISTIO) {
            throw new IllegalStateException("No se puede modificar una cita marcada como no asistida");
        }

        if (nuevoEstado == EstadoCita.EN_CURSO && cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new IllegalStateException("Solo las citas confirmadas pueden estar en curso");
        }

        if (nuevoEstado == EstadoCita.REPROGRAMADA &&
                cita.getEstado() != EstadoCita.PENDIENTE && cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new IllegalStateException("Solo las citas pendientes o confirmadas pueden ser reprogramadas");
        }

        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    // Eliminar una cita por ID
    public void eliminarCita(String id) {
        if (!citaRepository.existsById(id)) {
            throw new IllegalArgumentException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }

    // Obtener citas relacionadas con un usuario (médico o paciente)
    public List<Cita> obtenerCitasPorUsuario(String usuarioId) {
        return citaRepository.findByMedicoUsuarioIdOrPacienteId(usuarioId, usuarioId);
    }

    // Obtener citas de un paciente específico
    public List<Cita> obtenerCitasPorPaciente(String pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    
}

