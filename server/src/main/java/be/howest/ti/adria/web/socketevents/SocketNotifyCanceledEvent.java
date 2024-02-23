package be.howest.ti.adria.web.socketevents;

import be.howest.ti.adria.web.dto.EventDto;

public class SocketNotifyCanceledEvent extends SocketEvent {
    private final EventDto event;

    public EventDto getEvent() {
        return event;
    }

    public SocketNotifyCanceledEvent(EventDto event) {
        super(SocketEventType.CANCELEDEVENT);
        this.event = event;
    }
}
