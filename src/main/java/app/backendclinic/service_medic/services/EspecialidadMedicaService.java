package app.backendclinic.service_medic.services;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import app.backendclinic.service_medic.dtos.EspecialidadMedicaDto;
import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.repositorys.EspecialidadMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EspecialidadMedicaService {

    @Autowired
    private EspecialidadMedicaRepository repository;

    @Autowired
    private BitacoraService bitacoraService;

    @Transactional
    public EspecialidadMedica crearEspecialidad(EspecialidadMedicaDto dto, User usuario, String ipAddress) {
        try {
            EspecialidadMedica especialidad = EspecialidadMedica.builder()
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .build();
            EspecialidadMedica savedEspecialidad = repository.save(especialidad);

            // Registrar acción en la bitácora
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR ESPECIALIDAD MÉDICA", ipAddress, "Exitoso");

            return savedEspecialidad;
        } catch (Exception e) {
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR ESPECIALIDAD MÉDICA", ipAddress, "Fallido");
            throw new RuntimeException("Error al crear la especialidad médica", e);
        }
    }

    @Transactional
    public EspecialidadMedica actualizarEspecialidad(String id, EspecialidadMedicaDto dto, User usuario, String ipAddress) {
        try {
            Optional<EspecialidadMedica> optionalEspecialidad = repository.findById(id);
            if (optionalEspecialidad.isPresent()) {
                EspecialidadMedica especialidad = optionalEspecialidad.get();
                especialidad.setNombre(dto.getNombre());
                especialidad.setDescripcion(dto.getDescripcion());
                EspecialidadMedica updatedEspecialidad = repository.save(especialidad);

                bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZAR ESPECIALIDAD MÉDICA", ipAddress, "Exitoso");
                return updatedEspecialidad;
            } else {
                throw new RuntimeException("Especialidad no encontrada");
            }
        } catch (Exception e) {
            bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZAR ESPECIALIDAD MÉDICA", ipAddress, "Fallido");
            throw new RuntimeException("Error al actualizar la especialidad médica", e);
        }
    }

    @Transactional
    public void eliminarEspecialidad(String id, User usuario, String ipAddress) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                bitacoraService.registrarAccion("ELIMINACIÓN", usuario, "ELIMINAR ESPECIALIDAD MÉDICA", ipAddress, "Exitoso");
            } else {
                throw new RuntimeException("Especialidad no encontrada");
            }
        } catch (Exception e) {
            bitacoraService.registrarAccion("ELIMINACIÓN", usuario, "ELIMINAR ESPECIALIDAD MÉDICA", ipAddress, "Fallido");
            throw new RuntimeException("Error al eliminar la especialidad médica", e);
        }
    }

    @Transactional(readOnly = true)
    public Page<EspecialidadMedica> listarEspecialidades(Pageable pageable, User usuario, String ipAddress) {
        try {
            Page<EspecialidadMedica> especialidades = repository.findAll(pageable);

            bitacoraService.registrarAccion("VISTA", usuario, "LISTAR ESPECIALIDADES MÉDICAS", ipAddress, "Exitoso");

            return especialidades;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "LISTAR ESPECIALIDADES MÉDICAS", ipAddress, "Fallido");
            throw new RuntimeException("Error al listar las especialidades médicas", e);
        }
    }

    @Transactional(readOnly = true)
    public EspecialidadMedica obtenerEspecialidadPorId(String id, User usuario, String ipAddress) {
        try {
            EspecialidadMedica especialidad = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

            bitacoraService.registrarAccion("VISTA", usuario, "DETALLE ESPECIALIDAD MÉDICA", ipAddress, "Exitoso");

            return especialidad;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "DETALLE ESPECIALIDAD MÉDICA", ipAddress, "Fallido");
            throw new RuntimeException("Error al obtener la especialidad médica", e);
        }
    }
}
