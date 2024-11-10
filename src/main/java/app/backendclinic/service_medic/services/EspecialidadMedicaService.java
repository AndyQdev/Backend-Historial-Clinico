package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.Dtos.EspecialidadMedicaDto;
import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.repositorys.EspecialidadMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EspecialidadMedicaService {

    @Autowired
    private EspecialidadMedicaRepository repository;

    public EspecialidadMedica crearEspecialidad(EspecialidadMedicaDto dto) {
        try {
            EspecialidadMedica especialidad = EspecialidadMedica.builder()
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .build();
            return repository.save(especialidad);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la especialidad médica", e);
        }
    }

    public EspecialidadMedica actualizarEspecialidad(String id, EspecialidadMedicaDto dto) {
        try {
            Optional<EspecialidadMedica> optionalEspecialidad = repository.findById(id);
            if (optionalEspecialidad.isPresent()) {
                EspecialidadMedica especialidad = optionalEspecialidad.get();
                especialidad.setNombre(dto.getNombre());
                especialidad.setDescripcion(dto.getDescripcion());
                return repository.save(especialidad);
            } else {
                throw new RuntimeException("Especialidad no encontrada");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la especialidad médica", e);
        }
    }

    public void eliminarEspecialidad(String id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("Especialidad no encontrada");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la especialidad médica", e);
        }
    }

    public Page<EspecialidadMedica> listarEspecialidades(Pageable pageable) {
        try {
            return repository.findAll(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las especialidades médicas", e);
        }
    }

    public EspecialidadMedica obtenerEspecialidadPorId(String id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la especialidad médica", e);
        }
    }
}
