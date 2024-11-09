package app.backendclinic.config;

import app.backendclinic.service_medic.services.HistorialMedicoService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StartupConfig {

    private final HistorialMedicoService historialMedicoService;

    public StartupConfig(HistorialMedicoService historialMedicoService) {
        this.historialMedicoService = historialMedicoService;
    }

//    @Bean
//    public ApplicationRunner runOnStartup() {
//        return args -> {
//            historialMedicoService.crearHistorialesMedicosFaltantes();
//            System.out.println("Historiales médicos faltantes creados");
//        };
//    }
}

