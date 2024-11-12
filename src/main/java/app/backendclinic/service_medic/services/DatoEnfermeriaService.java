package app.backendclinic.service_medic.services;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
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
    private final BitacoraService bitacoraService;

    public DatoEnfermeriaService(DatoEnfermeriaRepository datoEnfermeriaRepository,
                                 HistorialMedicoRepository historialMedicoRepository,
                                 BitacoraService bitacoraService) {
        this.datoEnfermeriaRepository = datoEnfermeriaRepository;
        this.historialMedicoRepository = historialMedicoRepository;
        this.bitacoraService = bitacoraService;
    }

    @Transactional
    public DatoEnfermeria addDatoEnfermeria(String historialMedicoId, double peso, double estatura, double presion,
                                            double temperatura, double saturacion, double frecuenciaRespiratoria,
                                            double frecuenciaCardiaca, LocalDateTime fechaRegistro,
                                            User usuario, String ipAddress) {
        try {
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

            DatoEnfermeria savedDatoEnfermeria = datoEnfermeriaRepository.save(datoEnfermeria);

            // Registrar en la bitácora en caso de éxito
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR DATO ENFERMERÍA", ipAddress, "Exitoso");

            return savedDatoEnfermeria;
        } catch (Exception e) {
            // Registrar en la bitácora en caso de fallo
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREAR DATO ENFERMERÍA", ipAddress, "Fallido");
            throw new RuntimeException("Error al agregar dato de enfermería", e);
        }
    }
}
