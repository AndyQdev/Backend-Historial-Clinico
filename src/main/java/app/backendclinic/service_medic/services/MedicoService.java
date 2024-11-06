package app.backendclinic.service_medic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    public Medico getMedicoById(String id) {
        return medicoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Medico not found with id: " + id));
    }

    @Transactional
    public Medico createMedico(Medico medico) {
        medico.setId(UUID.randomUUID().toString());
        return medicoRepository.save(medico);
    }

    @Transactional
    public HorarioAtencion addHorarioToMedico(String medicoId, HorarioAtencion horario) {
        Medico medico = getMedicoById(medicoId);
        horario.setId(UUID.randomUUID().toString());
        horario.setMedico(medico);
        return horarioAtencionRepository.save(horario);
    }

    public List<HorarioAtencion> getHorariosByMedicoId(String medicoId) {
        Medico medico = getMedicoById(medicoId);
        return medico.getHorariosAtencion();
    }
}