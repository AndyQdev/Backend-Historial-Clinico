package app.backendclinic.service_medic.services;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import app.backendclinic.service_medic.models.AnotacionHistorial;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.repositorys.AnotacionHistorialRepository;
import app.backendclinic.service_medic.repositorys.HistorialMedicoRepository;
import app.backendclinic.service_medic.repositorys.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.backendclinic.service_medic.models.Medico;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AnotacionHistorialService {

    private final AnotacionHistorialRepository anotacionHistorialRepository;
    private final HistorialMedicoRepository historialMedicoRepository;
    private final MedicoRepository medicoRepository;
    private final BitacoraService bitacoraService;

    public AnotacionHistorialService(AnotacionHistorialRepository anotacionHistorialRepository,
                                     HistorialMedicoRepository historialMedicoRepository,
                                     MedicoRepository medicoRepository,
                                     BitacoraService bitacoraService) {
        this.anotacionHistorialRepository = anotacionHistorialRepository;
        this.historialMedicoRepository = historialMedicoRepository;
        this.medicoRepository = medicoRepository;
        this.bitacoraService = bitacoraService;
    }

    @Transactional
    public AnotacionHistorial addAnotacion(String historialMedicoId, String medicoId, String descripcion, String tratamiento, User usuario, String ipAddress) {
        try {
            HistorialMedico historialMedico = historialMedicoRepository.findById(historialMedicoId)
                    .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));
            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

            AnotacionHistorial anotacion = AnotacionHistorial.builder()
                    .id(UUID.randomUUID().toString())
                    .historialMedico(historialMedico)
                    .medico(medico)
                    .descripcion(descripcion)
                    .tratamiento(tratamiento)
                    .fechaAnotacion(LocalDateTime.now())
                    .build();

            AnotacionHistorial savedAnotacion = anotacionHistorialRepository.save(anotacion);

            // Registrar en la bitácora la acción de agregar una anotación en el historial
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR ANOTACIÓN HISTORIAL", ipAddress, "Exitoso");

            return savedAnotacion;
        } catch (Exception e) {
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR ANOTACIÓN HISTORIAL", ipAddress, "Fallido");
            throw new RuntimeException("Error al agregar anotación en el historial", e);
        }
    }
}
