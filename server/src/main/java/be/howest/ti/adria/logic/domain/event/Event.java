package be.howest.ti.adria.logic.domain.event;

import be.howest.ti.adria.logic.domain.Category;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.Sector;
import be.howest.ti.adria.web.dto.EventDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Event {
    private final int id;
    private final String name;
    private final EventType eventType;
    private final String description;
    private final int amountOfPeople;
    private final Category category;
    private final User organiser;
    private final List<User> attendees;
    private final Sector sector;
    private final Instant startDateTime;
    private final int hours;
    private boolean canceled = false;
    private Coordinate eventLocation = Coordinate.empty();

    public Event(int id, String name, EventType eventType, String description, int amountOfPeople, Category category, User organiser, List<User> attendees, Sector sector, Instant startDateTime, int hours) {
        this.id = id;
        this.name = name;
        this.eventType = eventType;
        this.description = description;
        this.amountOfPeople = amountOfPeople;
        this.category = category;
        this.organiser = organiser;
        this.attendees = attendees;
        this.sector = sector;
        this.startDateTime = startDateTime;
        this.hours = hours;
        this.eventLocation = Coordinate.empty();
    }
    public Event(int id, String name, EventType eventType, String description, int amountOfPeople, Category category, User organiser, Sector sector, List<User> attendees, Instant startDateTime, int hours, Coordinate eventLocation, boolean canceled) {
        this(id, name, eventType, description, amountOfPeople, category, organiser, attendees, sector, startDateTime, hours); // Reuse the other constructor
        this.eventLocation = eventLocation;
        this.canceled = canceled;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id == event.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public EventType getEventType() {
        return eventType;
    }
    public String getDescription() {
        return description;
    }
    public int getAmountOfPeople() {
        return amountOfPeople;
    }
    public Category getCategory() {
        return category;
    }
    public User getOrganiser() {
        return organiser;
    }
    public List<User> getAttendees() {
        return attendees;
    }
    public Sector getSector() {
        return sector;
    }
    public Coordinate getEventLocation() {
        return eventLocation;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public int getHours() {
        return hours;
    }

    public EventDto toDto() {
        return new EventDto(id, name, eventType, description, amountOfPeople, category, organiser.toSimpleDto(), attendees.size(), sector, startDateTime, hours, eventLocation);
    }

    public boolean hasLocation() {
        if (eventLocation == null) return false;
        return !eventLocation.isEmpty();
    }

    public boolean isCancelled() {
        return canceled;
    }

    public boolean isOngoing() {
        Instant now = Instant.now();

        Instant eventEnd = startDateTime.plus(hours, ChronoUnit.HOURS);

        return now.isAfter(startDateTime) && now.isBefore(eventEnd);
    }
}
