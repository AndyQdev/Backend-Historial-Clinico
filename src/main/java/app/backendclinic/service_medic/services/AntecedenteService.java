package app.backendclinic.service_medic.services;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
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
    private final BitacoraService bitacoraService;

    public AntecedenteService(AntecedenteRepository antecedenteRepository,
                              HistorialMedicoRepository historialMedicoRepository,
                              BitacoraService bitacoraService) {
        this.antecedenteRepository = antecedenteRepository;
        this.historialMedicoRepository = historialMedicoRepository;
        this.bitacoraService = bitacoraService;
    }

    @Transactional
    public Antecedente addAntecedente(String historialMedicoId, String descripcion, LocalDate fechaRegistro, User usuario, String ipAddress) {
        try {
            HistorialMedico historialMedico = historialMedicoRepository.findById(historialMedicoId)
                    .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));

            Antecedente antecedente = new Antecedente();
            antecedente.setId(UUID.randomUUID().toString());
            antecedente.setHistorialMedico(historialMedico);
            antecedente.setDescripcion(descripcion);
            antecedente.setFechaRegistro(fechaRegistro);

            Antecedente savedAntecedente = antecedenteRepository.save(antecedente);

            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR ANTECEDENTE", ipAddress, "Exitoso");

            return savedAntecedente;
        } catch (Exception e) {
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR ANTECEDENTE", ipAddress, "Fallido");
            throw new RuntimeException("Error al agregar antecedente", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Antecedente> getAntecedentesByHistorialMedico(String historialMedicoId, User usuario, String ipAddress) {
        try {
            List<Antecedente> antecedentes = antecedenteRepository.findByHistorialMedicoId(historialMedicoId);

            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA ANTECEDENTES", ipAddress, "Exitoso");

            return antecedentes;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA ANTECEDENTES", ipAddress, "Fallido");
            throw new RuntimeException("Error al consultar antecedentes", e);
        }
    }

    @Transactional
    public Antecedente updateAntecedente(String antecedenteId, Antecedente updatedAntecedente, User usuario, String ipAddress) {
        try {
            Antecedente antecedente = antecedenteRepository.findById(antecedenteId)
                    .orElseThrow(() -> new RuntimeException("Antecedente no encontrado"));

            antecedente.setDescripcion(updatedAntecedente.getDescripcion());
            antecedente.setFechaRegistro(updatedAntecedente.getFechaRegistro());

            Antecedente savedAntecedente = antecedenteRepository.save(antecedente);

            bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZAR ANTECEDENTE", ipAddress, "Exitoso");

            return savedAntecedente;
        } catch (Exception e) {
            bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZAR ANTECEDENTE", ipAddress, "Fallido");
            throw new RuntimeException("Error al actualizar antecedente", e);
        }
    }
}
