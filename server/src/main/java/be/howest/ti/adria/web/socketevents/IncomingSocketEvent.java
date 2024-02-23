package be.howest.ti.adria.web.socketevents;

public class IncomingSocketEvent extends SocketEvent{
    public String getAdriaId() {
        return adriaId;
    }

    private final String adriaId;
    protected IncomingSocketEvent(SocketEventType eventType, String adriaId) {
        super(eventType);
        this.adriaId = adriaId;
    }
}
