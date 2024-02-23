package be.howest.ti.adria.web.socketevents;


public class SocketJoinEvent extends IncomingSocketEvent{
    public String getJoinId() {
        return joinId;
    }

    private final String joinId;

    protected SocketJoinEvent(String adriaId, String joinId) {
        super(SocketEventType.JOIN, adriaId);
        this.joinId = joinId;
    }
}
