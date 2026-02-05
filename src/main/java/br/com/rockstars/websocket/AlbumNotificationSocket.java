package br.com.rockstars.websocket;

import br.com.rockstars.domain.dto.AlbumNotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket(path = "/ws/albums")
@ApplicationScoped
public class AlbumNotificationSocket {

    private final Map<String, WebSocketConnection> connections = new ConcurrentHashMap<>();

    @Inject
    ObjectMapper objectMapper;

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        connections.put(connection.id(), connection);
        Log.info("WebSocket conectado: " + connection.id());
    }

    @OnClose
    public void onClose(WebSocketConnection connection) {
        connections.remove(connection.id());
        Log.info("WebSocket desconectado: " + connection.id());
    }

    public void broadcast(AlbumNotificationDTO notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            connections.values().forEach(connection -> {
                connection.sendTextAndAwait(message);
            });
            Log.info("Broadcast enviado para " + connections.size() + " clientes");
        } catch (Exception e) {
            Log.error("Erro ao enviar broadcast", e);
        }
    }

    public int getConnectionCount() {
        return connections.size();
    }
}
