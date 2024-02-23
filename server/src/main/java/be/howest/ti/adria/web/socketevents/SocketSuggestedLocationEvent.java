package be.howest.ti.adria.web.socketevents;

import be.howest.ti.adria.web.dto.EventDto;
import be.howest.ti.adria.web.dto.SuggestedLocationDto;

public class SocketSuggestedLocationEvent extends SocketEvent {

    private final SuggestedLocationDto suggestedLocation;
    private final EventDto event;

    public SuggestedLocationDto getSuggestedLocation() {
        return suggestedLocation;
    }

    public EventDto getEvent() {
        return event;
    }

    public SocketSuggestedLocationEvent(SuggestedLocationDto suggestedLocation, EventDto eventDto) {
        super(SocketEventType.SUGGESTLOCATION);
        this.suggestedLocation = suggestedLocation;
        this.event = eventDto;
    }
}
