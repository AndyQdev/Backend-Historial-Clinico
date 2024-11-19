package app.backendclinic.atencioncitas.controllers;

import app.backendclinic.atencioncitas.models.EstadoCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.backendclinic.atencioncitas.dtos.CrearCitaRequestDTO;
import app.backendclinic.atencioncitas.models.Cita;
import app.backendclinic.atencioncitas.repositorys.CitasRepository;
import app.backendclinic.atencioncitas.services.CitaService;
import app.backendclinic.atencioncitas.services.NotificationSocketService;
import app.backendclinic.auth.TwilioService;
import app.backendclinic.service_medic.models.HorarioAtencion;
import app.backendclinic.service_medic.services.HorarioAtencionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
// @RequiredArgsConstructor
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private CitasRepository citaRepository;
    @Autowired
    private NotificationSocketService notificationSocketService;
    @Autowired
    private HorarioAtencionService horarioAtencionService;
    @Autowired
    private TwilioService twilioService; 
    @GetMapping
    public ResponseEntity<List<Cita>> obtenerTodasLasCitas() {
        List<Cita> citas = citaService.obtenerTodasLasCitas();
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable String id) {
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Cita> crearCita(@RequestBody CrearCitaRequestDTO crearCitaRequest) {
        Cita nuevaCita = citaService.crearCita(crearCitaRequest);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(
            @PathVariable String id,
            @RequestBody Cita citaActualizada) {
        try {
            Cita cita = citaService.actualizarCita(id, citaActualizada);
            return new ResponseEntity<>(cita, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable String id) {
        try {
            citaService.eliminarCita(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cita>> obtenerCitasPorUsuario(@PathVariable String usuarioId) {
        List<Cita> citas = citaService.obtenerCitasPorUsuario(usuarioId);
        if (citas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> obtenerCitasPorPaciente(@PathVariable String pacienteId) {
        List<Cita> citas = citaService.obtenerCitasPorPaciente(pacienteId);
        if (citas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstadoCita(
            @PathVariable String id,
            @RequestParam String estado) {
        try {
            EstadoCita nuevoEstado = EstadoCita.valueOf(estado.toUpperCase());
            Cita citaActualizada = citaService.actualizarEstadoCita(id, nuevoEstado);
            return new ResponseEntity<>(citaActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el estado no es válido
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Si la transición no está permitida
        }
    }

    @PatchMapping("/{id}/no_asistio")
    public ResponseEntity<Cita> marcarNoAsistio(@PathVariable String id) {
        try {
            Cita citaNoAsistio = citaService.actualizarEstadoCita(id, EstadoCita.NO_ASISTIO);
            return new ResponseEntity<>(citaNoAsistio, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Transición no permitida
        }
    }


    @PostMapping("/{id}/cancelar")
    public void cancelarCita(@PathVariable String id, @RequestBody String motivo) {
        // Lógica para cancelar la cita
        notificationSocketService.sendNotification("userId", "Cita Cancelada", motivo);
    }

    @DeleteMapping("/{id}/cancelar-y-reasignar")
    public ResponseEntity<String> cancelarYReasignarCita(
            @PathVariable String id,
            @RequestBody String motivo) {
        try {
            // Obtener la cita que se va a cancelar
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));

            // Guardar el ID del médico y del paciente
            String usuarioId = cita.getMedico().getUsuario().getId();
            String pacienteId = cita.getPaciente().getId();
            String medicoId = cita.getMedico().getId();

            // Cancelar la cita (puedes eliminarla o actualizar su estado)
            citaRepository.deleteById(id);

            // Obtener horarios disponibles del médico
            // List<HorarioAtencion> horariosDisponibles = horarioAtencionService.getHorariosByUsuarioId(usuarioId);
            // System.out.println(horariosDisponibles);
            // if (horariosDisponibles.isEmpty()) {
                // throw new RuntimeException("No hay horarios disponibles para reasignar la cita.");
            // }

            // Seleccionar el primer horario disponible
            // HorarioAtencion nuevoHorario = horariosDisponibles.get(0);
            // System.out.println("Nuevo horario seleccionado: " + nuevoHorario);
            // Crear la nueva cita
            CrearCitaRequestDTO nuevaCitaRequest = new CrearCitaRequestDTO();
            nuevaCitaRequest.setMedicoId(medicoId);
            nuevaCitaRequest.setPacienteId(pacienteId);
            nuevaCitaRequest.setServicioId(cita.getServicioMedico().getId());
            nuevaCitaRequest.setEspecialidadId(cita.getEspecialidad().getId());
            Cita nuevaCita = citaService.crearCita(nuevaCitaRequest);

            // Enviar mensaje por WhatsApp
            String mensaje = String.format(
                    "Estimado %s %s, su cita ha sido cancelada por el siguiente motivo: %s. " +
                            "Se ha reasignado una nueva cita para el día %s de %s a %s.",
                    cita.getPaciente().getNombre(),
                    cita.getPaciente().getApellido(),
                    motivo,
                    "Viernes",
                    "14:00pm",
                    "15:00pm"
            );

            System.out.println(nuevaCita);
            twilioService.sendCustomMessage(cita.getPaciente().getTelefono(), mensaje);

            return ResponseEntity.ok("Cita cancelada y nueva cita reasignada con éxito.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cancelar y reasignar la cita: " + e.getMessage());
        }
    }
}