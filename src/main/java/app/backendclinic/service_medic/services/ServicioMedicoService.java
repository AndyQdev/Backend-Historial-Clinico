package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.models.ServiceMedico;
import app.backendclinic.service_medic.repositorys.EspecialidadMedicaRepository;
import app.backendclinic.service_medic.repositorys.ServicioMedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioMedicoService {

    @Autowired
    private ServicioMedicoRepository servicioMedicoRepository;

    @Autowired
    private EspecialidadMedicaRepository especialidadMedicaRepository;
    public ServiceMedico crearServicio(ServiceMedico servicioMedico) {
        return servicioMedicoRepository.save(servicioMedico);
    }

    public List<ServiceMedico> listarServicios() {
        return servicioMedicoRepository.findAll();
    }

    public ServiceMedico asignarEspecialidad(String servicioId, String especialidadId) {
    Optional<ServiceMedico> servicioOpt = servicioMedicoRepository.findById(servicioId);
    Optional<EspecialidadMedica> especialidadOpt = especialidadMedicaRepository.findById(especialidadId);

    if (servicioOpt.isPresent() && especialidadOpt.isPresent()) {
        ServiceMedico servicioMedico = servicioOpt.get();
        EspecialidadMedica especialidadMedica = especialidadOpt.get();

        // Asignar la especialidad al servicio médico
        servicioMedico.getEspecialidades().add(especialidadMedica);
        return servicioMedicoRepository.save(servicioMedico);
    } else {
        throw new RuntimeException("Servicio o Especialidad no encontrado");
    }
    }
    public ServiceMedico actualizarServicio(String id, ServiceMedico servicioMedicoActualizado) {
        Optional<ServiceMedico> servicioExistente = servicioMedicoRepository.findById(id);
        if (servicioExistente.isPresent()) {
            ServiceMedico servicio = servicioExistente.get();
            servicio.setNombre(servicioMedicoActualizado.getNombre());
            servicio.setPrecio(servicioMedicoActualizado.getPrecio());
            servicio.setEspecialidades(servicioMedicoActualizado.getEspecialidades());
            return servicioMedicoRepository.save(servicio);
        } else {
            throw new RuntimeException("Servicio médico no encontrado");
        }
    }

    public void eliminarServicio(String id) {
        servicioMedicoRepository.deleteById(id);
    }
    public List<EspecialidadMedica> obtenerEspecialidadesPorServicio(String servicioId) {
        ServiceMedico servicio = servicioMedicoRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        return servicio.getEspecialidades();
    }
}
