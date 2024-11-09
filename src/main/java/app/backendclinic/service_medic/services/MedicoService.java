package app.backendclinic.service_medic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.backendclinic.models.User;
import app.backendclinic.repositorys.UserRepository;
import app.backendclinic.service_medic.models.HorarioAtencion;
import app.backendclinic.service_medic.models.Medico;
import app.backendclinic.service_medic.repositorys.HorarioAtencionRepository;
import app.backendclinic.service_medic.repositorys.MedicoRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private HorarioAtencionRepository horarioAtencionRepository;

    @Autowired
    private UserRepository userRepository;
    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    public Medico getMedicoById(String id) {
        return medicoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Medico not found with id: " + id));
    }

    @Transactional
    public Medico createMedico(String usuarioId) {
        // Buscar el Usuario por su ID
        User usuario = userRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario not found with id: " + usuarioId));

        // Crear el Medico y asociar el Usuario
        Medico medico = Medico.builder()
            .id(UUID.randomUUID().toString())
            .usuario(usuario)
            .build();

        return medicoRepository.save(medico);
    }

    @Transactional
    public HorarioAtencion addHorarioToMedico(String medicoId, HorarioAtencion horario) {
        Medico medico = getMedicoById(medicoId);

        // Validar que no exista superposición de horarios en el mismo día
        boolean hasOverlap = medico.getHorariosAtencion().stream().anyMatch(existingHorario -> 
            existingHorario.getDia().equals(horario.getDia()) && 
            !(horario.getHoraFin().isBefore(existingHorario.getHoraInicio()) || 
              horario.getHoraInicio().isAfter(existingHorario.getHoraFin()))
        );

        if (hasOverlap) {
            throw new IllegalArgumentException("El médico ya tiene un horario asignado en este día y hora.");
        }

        // Si no hay superposición, agregar el nuevo horario
        horario.setId(UUID.randomUUID().toString());
        horario.setMedico(medico);
        return horarioAtencionRepository.save(horario);
    }

    public List<HorarioAtencion> getHorariosByMedicoId(String medicoId) {
        Medico medico = getMedicoById(medicoId);
        return medico.getHorariosAtencion();
    }
}