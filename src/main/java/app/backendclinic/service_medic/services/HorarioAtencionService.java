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
            String medicoId = medico.getId();
            // Obtener la primera especialidad y el primer servicio asociado para simplificar
            String specialty = medico.getEspecialidades().isEmpty() ? "N/A" : medico.getEspecialidades().get(0).getNombre();
            String descripcionEspecialidad = medico.getEspecialidades().isEmpty() ? "N/A" : medico.getEspecialidades().get(0).getDescripcion();
            String specialtyId =  medico.getEspecialidades().isEmpty() ? "N/A" : medico.getEspecialidades().get(0).getId();
            String service = "N/A";
            String descripcionServicio = "N/A";
            Double precioServicio = null;
            String serviceId = "N/A";
            if (!medico.getEspecialidades().isEmpty() && !medico.getEspecialidades().get(0).getServicios().isEmpty()) {
                service = medico.getEspecialidades().get(0).getServicios().get(0).getNombre();
                serviceId = medico.getEspecialidades().get(0).getServicios().get(0).getId();
                descripcionServicio = medico.getEspecialidades().get(0).getServicios().get(0).getDescripcion();
                precioServicio = medico.getEspecialidades().get(0).getServicios().get(0).getPrecio();
            }
    
            return new HorarioAtencionDTO(
                    horario.getId(),
                    horario.getDia(),
                    horario.getHoraInicio().toString(),
                    horario.getHoraFin().toString(),
                    doctorName,
                    service,
                    specialty,
                    rating,
                    descripcionServicio,
                    precioServicio,
                    descripcionEspecialidad,
                    medicoId,
                    specialtyId,
                    serviceId
            );
        }).collect(Collectors.toList());
    }
    
}
