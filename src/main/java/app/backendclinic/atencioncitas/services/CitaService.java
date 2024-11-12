package app.backendclinic.atencioncitas.services;


import app.backendclinic.atencioncitas.dtos.CrearCitaRequestDTO;
import app.backendclinic.atencioncitas.models.Cita;
import app.backendclinic.atencioncitas.repositorys.CitasRepository;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.repositorys.PacienteRepository;
import app.backendclinic.service_medic.models.Medico;
import app.backendclinic.service_medic.models.ServiceMedico;
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
    private ServicioMedicoRepository serviceMedicoRepository;

    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerCitaPorId(String id) {
        return citaRepository.findById(id);
    }

    public Cita crearCita(CrearCitaRequestDTO crearCitaRequest) {
        // Extraer IDs y otros atributos desde el DTO
        String medicoId = crearCitaRequest.getMedicoId();
        String pacienteId = crearCitaRequest.getPacienteId();
        String servicioId = crearCitaRequest.getServicioId();
        
        // Lógica de creación de la cita usando estos IDs y otros datos
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado"));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));
        ServiceMedico servicio = serviceMedicoRepository.findById(servicioId)
                .orElseThrow(() -> new EntityNotFoundException("Servicio médico no encontrado"));

        Cita nuevaCita = new Cita();
        nuevaCita.setMedico(medico);
        nuevaCita.setPaciente(paciente);
        nuevaCita.setServicioMedico(servicio);
        nuevaCita.setFechaCreada(LocalDateTime.now());
        nuevaCita.setEspecialidad(crearCitaRequest.getEspecialidad());
        // nuevaCita.setCodigoCita(crearCitaRequest.getCodigoCita()); // o generarlo aquí si no lo recibe

        return citaRepository.save(nuevaCita);
    }

    public Cita actualizarCita(String id, Cita citaActualizada) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));

        cita.setMedico(citaActualizada.getMedico());
        cita.setPaciente(citaActualizada.getPaciente());
        cita.setServicioMedico(citaActualizada.getServicioMedico());

        return citaRepository.save(cita);
    }

    public void eliminarCita(String id) {
        if (!citaRepository.existsById(id)) {
            throw new IllegalArgumentException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }
}
