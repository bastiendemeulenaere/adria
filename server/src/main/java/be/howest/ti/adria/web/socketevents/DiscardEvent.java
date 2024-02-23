package be.howest.ti.adria.web.socketevents;

public class DiscardEvent extends IncomingSocketEvent {
    protected DiscardEvent(String adriaId) {
        super(SocketEventType.DISCARD, adriaId);
    }
}
