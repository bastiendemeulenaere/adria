package be.howest.ti.adria.web.socketevents;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.web.dto.EventDto;

public class SocketNotifyLocationEvent extends SocketEvent {

    private final Coordinate location;
    private final EventDto event;
    public Coordinate getLocation() {
        return location;
    }
    public EventDto getEvent() {
        return event;
    }

    public SocketNotifyLocationEvent(Coordinate location, EventDto eventDto) {
        super(SocketEventType.NOTIFYLOCATION);
        this.location = location;
        this.event = eventDto;
    }
}
