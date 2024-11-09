package app.backendclinic.service_medic.services;

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

    public AnotacionHistorialService(AnotacionHistorialRepository anotacionHistorialRepository,
                                     HistorialMedicoRepository historialMedicoRepository,
                                     MedicoRepository medicoRepository) {
        this.anotacionHistorialRepository = anotacionHistorialRepository;
        this.historialMedicoRepository = historialMedicoRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public AnotacionHistorial addAnotacion(String historialMedicoId, String medicoId, String descripcion, String tratamiento) {
        HistorialMedico historialMedico = historialMedicoRepository.findById(historialMedicoId)
                .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        AnotacionHistorial anotacion = AnotacionHistorial.builder()
                .id(UUID.randomUUID().toString())
                .historialMedico(historialMedico)
                .medico(medico)
                .descripcion(descripcion)
                .tratamiento(tratamiento) // Asigna el tratamiento
                .fechaAnotacion(LocalDateTime.now())
                .build();

        return anotacionHistorialRepository.save(anotacion);
    }
}
