package be.howest.ti.adria.web.socketevents;
public abstract class SocketEvent {
    public SocketEventType getEventType() {
        return eventType;
    }
    private final SocketEventType eventType;
    protected SocketEvent(SocketEventType type){
        this.eventType = type;
    }
}
