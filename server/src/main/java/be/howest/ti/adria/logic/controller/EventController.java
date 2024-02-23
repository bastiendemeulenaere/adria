package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.web.dto.EventCreateDto;

import java.util.List;

public interface EventController {
    Event createEvent(EventCreateDto event, String adriaId);

    List<Event> getEvents();

    List<Event> getEvents(boolean includePast);

    List<Event> getOngoingEvents(String id);

    List<Event> getUpComingEvents(String id);

    Event getEventById(int id);

    List<Event> getCreatedEventsById(String id);

    Event joinEvent(String adriaId, int eventId);

    Event leaveEvent(String adriaId, int eventId);

    List<Event> getEventsByUserId(String adriaId);

    Event cancelEvent(int eventId);

    Event editEvent(int eventId, EventCreateDto eventDto);

    List<Event> getEventsFromSector(int sectorNumber);

    List<Event> getEventsFromSector(int sectorNumber, boolean includePast);

    Event addCoordinateToEvent(int eventId, Coordinate coordinate);

    boolean hasUserJoinedEvent(String adriaId, int eventId);
}
