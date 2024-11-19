package app.backendclinic.atencioncitas.services;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationSocketService {

    private final SocketIOServer server;

    public NotificationSocketService(SocketIOServer server) {
        this.server = server;
        System.out.println(server);
        System.out.println("ENTROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");       
        this.server.start(); // Iniciar el servidor al arrancar la aplicación
    }

    @OnConnect
    public void onConnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Cliente conectado: " + client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(com.corundumstudio.socketio.SocketIOClient client) {
        System.out.println("Cliente desconectado: " + client.getSessionId());
    }

    @OnEvent("notificacion")
    public void onNotification(com.corundumstudio.socketio.SocketIOClient client, String message) {
        System.out.println("Notificación recibida: " + message);

        // Enviar notificación a todos los clientes
        server.getBroadcastOperations().sendEvent("notificacion", "Mensaje enviado a todos los clientes");
    }

    public void sendNotification(String userId, String title, String content) {
        
        server.getBroadcastOperations().sendEvent("notificacion", String.format("%s: %s", title, content));
    }
}