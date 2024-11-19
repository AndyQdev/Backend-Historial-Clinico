package app.backendclinic.atencioncitas.services;


import app.backendclinic.atencioncitas.dtos.RegistrarAtencionDTO;
import app.backendclinic.atencioncitas.models.AtencionEspecialidad;
import app.backendclinic.atencioncitas.models.Cita;
import app.backendclinic.atencioncitas.repositorys.AtencionEspecialidadRepository;
import app.backendclinic.atencioncitas.repositorys.CitasRepository;
import app.backendclinic.service_medic.models.AnotacionHistorial;
import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.repositorys.AnotacionHistorialRepository;
import app.backendclinic.service_medic.repositorys.EspecialidadMedicaRepository;
import app.backendclinic.service_medic.repositorys.HistorialMedicoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AtencionService {

    @Autowired
    private CitasRepository citaRepository;

    @Autowired
    private EspecialidadMedicaRepository especialidadRepository;

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;

    @Autowired
    private AtencionEspecialidadRepository atencionEspecialidadRepository;

    @Autowired
    private AnotacionHistorialRepository anotacionHistorialRepository;

    public AtencionEspecialidad registrarAtencion(RegistrarAtencionDTO dto) {
        try {
            // Verificar que la cita existe
            Cita cita = citaRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));

            // Verificar que la especialidad existe
            EspecialidadMedica especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                    .orElseThrow(() -> new EntityNotFoundException("Especialidad no encontrada"));

            // Obtener el historial médico del paciente
            HistorialMedico historial = historialMedicoRepository.findOptionalByPacienteId(cita.getPaciente().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Historial médico no encontrado"));

            // Crear una nueva atención
            AtencionEspecialidad atencion = new AtencionEspecialidad();
            atencion.setCita(cita);
            atencion.setEspecialidad(especialidad);
            atencion.setHistorialMedico(historial);
            atencion.setDatosFormulario(new ObjectMapper().writeValueAsString(dto.getDatosFormulario()));
            atencion.setFechaAtencion(LocalDateTime.now());

            // Guardar la atención
            atencionEspecialidadRepository.save(atencion);

            // Crear una anotación en el historial médico
            AnotacionHistorial anotacion = new AnotacionHistorial();
            anotacion.setHistorialMedico(historial);
            anotacion.setMedico(cita.getMedico()); // Relacionar con el médico de la cita
            anotacion.setDescripcion("Atención realizada en la especialidad: " + especialidad.getNombre());
            anotacion.setTratamiento((String) dto.getDatosFormulario().getOrDefault("tratamiento", "No especificado"));
            anotacion.setFechaAnotacion(LocalDateTime.now());

            // Guardar la anotación
            anotacionHistorialRepository.save(anotacion);

            return atencion;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar el formulario en formato JSON", e);
        }
    }
}
