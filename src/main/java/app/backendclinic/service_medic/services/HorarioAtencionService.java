package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.dtos.HorarioAtencionDTO;
import app.backendclinic.service_medic.models.HorarioAtencion;
import app.backendclinic.service_medic.models.Medico;
import app.backendclinic.service_medic.models.ServiceMedico;
import app.backendclinic.service_medic.repositorys.HorarioAtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioAtencionService {

    @Autowired
    private HorarioAtencionRepository horarioAtencionRepository;

    public List<HorarioAtencionDTO> getAllHorariosWithDetails() {
        List<HorarioAtencion> horarios = horarioAtencionRepository.findAll();

        return horarios.stream().map(horario -> {
            Medico medico = horario.getMedico();
            String doctorName = medico.getUsuario().getNombre();
            Double rating = medico.getRating();

            // Obtener la primera especialidad y el primer servicio asociado para simplificar
            String specialty = medico.getEspecialidades().isEmpty() ? "N/A" : medico.getEspecialidades().get(0).getNombre();
            String service = medico.getEspecialidades().isEmpty() || medico.getEspecialidades().get(0).getServicios().isEmpty()
                    ? "N/A"
                    : medico.getEspecialidades().get(0).getServicios().get(0).getNombre();

            return new HorarioAtencionDTO(
                    horario.getId(),
                    horario.getDia(),
                    horario.getHoraInicio().toString(),
                    horario.getHoraFin().toString(),
                    doctorName,
                    service,
                    specialty,
                    rating
            );
        }).collect(Collectors.toList());
    }
}
