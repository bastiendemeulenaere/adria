package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.EventRepository;
import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.domain.event.EventType;
import be.howest.ti.adria.web.dto.EventCreateDto;

import java.util.List;
import java.util.NoSuchElementException;

public class EventControllerImpl implements EventController {
    private static final String MSG_UNKNOWN_EVENT = "No event(s) available";
    private final EventRepository eventRepository;

    public EventControllerImpl(EventRepository repository) {
        this.eventRepository = repository;
    }

    @Override
    public Event createEvent(EventCreateDto event, String adriaId) {
        if (event == null)
            throw new IllegalArgumentException("An empty Event is not allowed.");

        return eventRepository.createEvent(event, adriaId, EventType.NORMAL); //a default event is normal
    }
    @Override
    public List<Event> getEvents(){return getEvents(false);}
    @Override
    public List<Event> getEvents(boolean includePast) {
        List<Event> events = eventRepository.getEvents(includePast);

        if (null == events)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return events;
    }

    @Override
    public List<Event> getOngoingEvents(String id){
        List<Event> ongoingEvents = eventRepository.getOngoingEvents(id);


        if (null == ongoingEvents)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return ongoingEvents;
    }

    @Override
    public List<Event> getUpComingEvents(String id) {
        List<Event> upcomingEvents = eventRepository.getUpComingEvents(id);

        if (null == upcomingEvents)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return upcomingEvents;
    }

    @Override
    public Event getEventById(int id) {
        Event event = eventRepository.getEventById(id);

        if (null == event)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return event;
    }

    @Override
    public List<Event> getCreatedEventsById(String id) {
        List<Event> events = eventRepository.getCreatedEventsById(id);

        if (null == events)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return events;
    }


    @Override
    public Event joinEvent(String adriaId, int eventId) {
        Event event = eventRepository.joinEvent(adriaId, eventId);

        if (null == event)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return event;
    }

    @Override
    public Event leaveEvent(String adriaId, int eventId) {
        Event event = eventRepository.leaveEvent(adriaId, eventId);

        if (null == event)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return event;
    }

    @Override
    public List<Event> getEventsByUserId(String adriaId) {
        List<Event> ongoingEvents = eventRepository.getEventsByUserId(adriaId);


        if (null == ongoingEvents)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return ongoingEvents;
    }

    @Override
    public Event cancelEvent(int eventId) {
        Event event = eventRepository.cancelEvent(eventId);

        if (null == event)
            throw new NoSuchElementException(MSG_UNKNOWN_EVENT);

        return event;
    }

    @Override
    public Event editEvent(int eventId, EventCreateDto eventDto) {
        if (eventDto == null)
            throw new IllegalArgumentException("An empty Event is not allowed.");

        return eventRepository.editEvent(eventDto, eventId, EventType.NORMAL);
    }

    @Override
    public List<Event> getEventsFromSector(int sectorNumber) {
        return getEventsFromSector(sectorNumber, false);
    }
    @Override
    public List<Event> getEventsFromSector(int sectorNumber, boolean includePast) {
        return eventRepository.getEventsFromSector(sectorNumber, includePast);
    }
    @Override
    public Event addCoordinateToEvent(int eventId, Coordinate coordinate){
        return eventRepository.setEventLocation(eventId, coordinate);
    }
    @Override
    public boolean hasUserJoinedEvent(String adriaId, int eventId) {
        return eventRepository.hasUserJoinedEvent(adriaId, eventId);
    }
}
