package be.howest.ti.adria.web.dto;

import be.howest.ti.adria.logic.domain.Category;
import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.Sector;
import be.howest.ti.adria.logic.domain.event.EventType;
import be.howest.ti.adria.web.dto.adria.SectorDto;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.time.Instant;
public class EventDto {
    private int id;
    private final String name;
    private final EventType eventType;
    private final String description;
    private final int amountOfPeople;
    private final Category category;
    private final int attendees;
    private final SimpleUserDto organiser;
    private final Sector sector;
    private final long startDateTime;
    private final int hours;
    private Coordinate location = Coordinate.empty();


    private EventDto(String name, EventType eventType, String description, int amountOfPeople, Category category, SimpleUserDto organiser, int attendees, Sector sector, Instant startDateTime, int hours, Coordinate location) {
        this.name = name;
        this.eventType = eventType;
        this.description = description;
        this.amountOfPeople = amountOfPeople;
        this.category = category;
        this.organiser = organiser;
        this.attendees = attendees;
        this.sector = sector;
        this.startDateTime = startDateTime.toEpochMilli();
        this.hours = hours;
        this.location = location;
    }
    private EventDto(String name, EventType eventType, String description, int amountOfPeople, Category category, SimpleUserDto organiser, int attendees, Sector sector, Instant startDateTime, int hours) {
        this.name = name;
        this.eventType = eventType;
        this.description = description;
        this.amountOfPeople = amountOfPeople;
        this.category = category;
        this.organiser = organiser;
        this.attendees = attendees;
        this.sector = sector;
        this.startDateTime = startDateTime.toEpochMilli();
        this.hours = hours;
    }


    public EventDto(int id, String name, EventType eventType, String description, int amountOfPeople, Category category, SimpleUserDto organiser, int attendees, Sector sector, Instant startDateTime, int hours) {
        this(name, eventType, description, amountOfPeople, category, organiser, attendees, sector, startDateTime, hours);
        this.id = id;
    }
    public EventDto(int id, String name, EventType eventType, String description, int amountOfPeople, Category category, SimpleUserDto organiser, int attendees, Sector sector, Instant startDateTime, int hours, Coordinate location) {
        this(name, eventType, description, amountOfPeople, category, organiser, attendees, sector, startDateTime, hours, location);
        this.id = id;
    }
    @JsonGetter("id")
    public int getId() {
        return id;
    }
    @JsonGetter("name")
    public String getName() {
        return name;
    }
    @JsonGetter("eventType")
    public EventType getEventType() {
        return eventType;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    @JsonGetter("amountOfPeople")
    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public Category getCategory() {
        return category;
    }
    @JsonGetter("organiser")
    public SimpleUserDto getOrganiser() {
        return organiser;
    }
    @JsonGetter("sector")
    public SectorDto getSector() {
        return sector.toDto();
    }
    @JsonGetter("startDateTime")
    public long getStartDateTime() {
        return startDateTime;
    }
    @JsonGetter("hours")
    public int getHours() {
        return hours;
    }
    @JsonGetter("location")
    public Coordinate getLocation(){
        return location;
    }
    @JsonGetter("attendees")
    public int getAttendees() {
        return attendees;
    }
}
