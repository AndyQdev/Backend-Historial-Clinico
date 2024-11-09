package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.models.Antecedente;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.repositorys.AntecedenteRepository;
import app.backendclinic.service_medic.repositorys.HistorialMedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AntecedenteService {

    private final AntecedenteRepository antecedenteRepository;
    private final HistorialMedicoRepository historialMedicoRepository;

    public AntecedenteService(AntecedenteRepository antecedenteRepository, HistorialMedicoRepository historialMedicoRepository) {
        this.antecedenteRepository = antecedenteRepository;
        this.historialMedicoRepository = historialMedicoRepository;
    }

    @Transactional
    public Antecedente addAntecedente(String historialMedicoId, String descripcion, LocalDate fechaRegistro) {
        HistorialMedico historialMedico = historialMedicoRepository.findById(historialMedicoId)
                .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));

        Antecedente antecedente = new Antecedente();
        antecedente.setId(UUID.randomUUID().toString());
        antecedente.setHistorialMedico(historialMedico);
        antecedente.setDescripcion(descripcion);
        antecedente.setFechaRegistro(fechaRegistro);

        return antecedenteRepository.save(antecedente);
    }

    @Transactional(readOnly = true)
    public List<Antecedente> getAntecedentesByHistorialMedico(String historialMedicoId) {
        // Obtener y retornar todos los antecedentes asociados al historial médico
        return antecedenteRepository.findByHistorialMedicoId(historialMedicoId);
    }
    @Transactional
    public Antecedente updateAntecedente(String antecedenteId, Antecedente updatedAntecedente) {
        Antecedente antecedente = antecedenteRepository.findById(antecedenteId)
                .orElseThrow(() -> new RuntimeException("Antecedente no encontrado"));

        antecedente.setDescripcion(updatedAntecedente.getDescripcion());
        antecedente.setFechaRegistro(updatedAntecedente.getFechaRegistro());

        return antecedenteRepository.save(antecedente);
    }
}
