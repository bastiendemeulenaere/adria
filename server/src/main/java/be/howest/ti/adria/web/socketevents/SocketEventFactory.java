package be.howest.ti.adria.web.socketevents;

import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class SocketEventFactory {
    private static final SocketEventFactory instance = new SocketEventFactory();
    public static SocketEventFactory getInstance() {
        return instance;
    }


    public IncomingSocketEvent createIncomingEvent(JsonObject json) {
        SocketEventType eventType = SocketEventType.fromString(json.getString("type"));
        String adriaId = json.getString("adriaId");
        IncomingSocketEvent event = new DiscardEvent(adriaId);
        if (Objects.requireNonNull(eventType) == SocketEventType.JOIN) {
            event = new SocketJoinEvent(adriaId, json.getString("joinId"));
        }
        return event;
    }
}
