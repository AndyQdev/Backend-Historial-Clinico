package app.backendclinic.config;


// import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;


import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost"); // Cambiar según tu entorno
        config.setPort(9092);            // Cambiar según tu configuración
        return new SocketIOServer(config);
    }

}
