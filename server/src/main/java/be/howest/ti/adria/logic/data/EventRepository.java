package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.domain.event.EventType;
import be.howest.ti.adria.web.dto.EventCreateDto;

import java.util.List;

public abstract class EventRepository extends Repository{
    protected EventRepository(H2Connection h2Connection) {
        super(h2Connection);
    }
    public abstract Event createEvent(EventCreateDto event, String organiserId, EventType eventType);
    public abstract Event getEventById(int id);
    public abstract boolean hasUserJoinedEvent(String userId, int eventId);
    public abstract List<Event> getOngoingEvents(String id);

    public abstract List<Event> getEventsByUserId(String id);

    public abstract List<Event> getEvents(boolean includePast);

    public abstract Event joinEvent(String adriaId, int eventId);

    public abstract List<Event> getUpComingEvents(String id);

    public abstract List<Event> getCreatedEventsById(String id);

    public abstract Event leaveEvent(String adriaId, int eventId);

    public abstract List<Event> getEventsFromSector(int sectorNumber, boolean includePast);

    public abstract Event cancelEvent(int eventId);

    public abstract Event editEvent(EventCreateDto eventDto, int eventId, EventType eventType);
    public abstract List<Event> getEventsFromSector(int sectorNumber);
    public abstract Event setEventLocation(int eventId, Coordinate coordinate);
}
