package app.backendclinic.service_medic.services;

import app.backendclinic.service_medic.models.DatoEnfermeria;
import app.backendclinic.service_medic.models.HistorialMedico;
import app.backendclinic.service_medic.repositorys.DatoEnfermeriaRepository;
import app.backendclinic.service_medic.repositorys.HistorialMedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class DatoEnfermeriaService {

    private final DatoEnfermeriaRepository datoEnfermeriaRepository;
    private final HistorialMedicoRepository historialMedicoRepository;

    public DatoEnfermeriaService(DatoEnfermeriaRepository datoEnfermeriaRepository, HistorialMedicoRepository historialMedicoRepository) {
        this.datoEnfermeriaRepository = datoEnfermeriaRepository;
        this.historialMedicoRepository = historialMedicoRepository;
    }

    @Transactional
    public DatoEnfermeria addDatoEnfermeria(String historialMedicoId, double peso, double estatura, double presion,
                                            double temperatura, double saturacion, double frecuenciaRespiratoria,
                                            double frecuenciaCardiaca, LocalDateTime fechaRegistro) {
        HistorialMedico historialMedico = historialMedicoRepository.findById(historialMedicoId)
                .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));

        DatoEnfermeria datoEnfermeria = new DatoEnfermeria();
        datoEnfermeria.setId(UUID.randomUUID().toString());
        datoEnfermeria.setHistorialMedico(historialMedico);
        datoEnfermeria.setPeso(peso);
        datoEnfermeria.setEstatura(estatura);
        datoEnfermeria.setPresion(presion);
        datoEnfermeria.setTemperatura(temperatura);
        datoEnfermeria.setSaturacion(saturacion);
        datoEnfermeria.setFrecuenciaRespiratoria(frecuenciaRespiratoria);
        datoEnfermeria.setFrecuenciaCardiaca(frecuenciaCardiaca);
        datoEnfermeria.setFechaRegistro(fechaRegistro);

        // Calcular y asignar el IMC
        datoEnfermeria.setImc(peso / (estatura * estatura));

        return datoEnfermeriaRepository.save(datoEnfermeria);
    }
}
