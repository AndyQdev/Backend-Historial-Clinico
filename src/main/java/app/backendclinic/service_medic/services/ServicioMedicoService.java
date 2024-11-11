package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.models.ServiceMedico;
import app.backendclinic.service_medic.repositorys.ServicioMedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioMedicoService {

    @Autowired
    private ServicioMedicoRepository servicioMedicoRepository;

    public ServiceMedico crearServicio(ServiceMedico servicioMedico) {
        return servicioMedicoRepository.save(servicioMedico);
    }

    public List<ServiceMedico> listarServicios() {
        return servicioMedicoRepository.findAll();
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
}
