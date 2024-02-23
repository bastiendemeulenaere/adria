package be.howest.ti.adria.web.socketevents;

import be.howest.ti.adria.web.dto.EventDto;
import be.howest.ti.adria.web.dto.adria.DangerousAreaDto;

public class SocketNotifyDangerEvent extends SocketEvent {
    private final EventDto event;
    private final DangerousAreaDto dangerousArea;

    public EventDto getEvent() {
        return event;
    }

    public DangerousAreaDto getDangerousArea() {
        return dangerousArea;
    }

    public SocketNotifyDangerEvent(EventDto event, DangerousAreaDto dangerousAreaDto) {
        super(SocketEventType.DANGEREVENT);
        this.event = event;
        this.dangerousArea = dangerousAreaDto;
    }
}
