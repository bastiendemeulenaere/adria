package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.UserCreateObject;
import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.web.dto.EventCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventControllerTest extends BaseControllerTest{
    private EventController eventController;
    private int pastEventId;
    private static final String TEST_USER_ID = "test";
    @BeforeEach
    void setup(){
        this.eventController = new EventControllerImpl(Repositories.getInstance().getEventRepository());
        UserController userController = new UserController(Repositories.getInstance().getUserRepository());
        //login and register
        UserCreateObject userCreateObject = new UserCreateObject(TEST_USER_ID, "firstname", "lastname", "aboutme");
        userController.createUser(userCreateObject);

        //Make some test events
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().toEpochMilli(), 4);
        EventCreateDto eventCreateDtoPast = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().minus(50, ChronoUnit.MINUTES).toEpochMilli(), 4);
        eventController.createEvent(eventCreateDto, TEST_USER_ID).getId();
        pastEventId = eventController.createEvent(eventCreateDtoPast, TEST_USER_ID).getId();
    }

    @Test
    void createEvent() {
        // Test for createEvent method
        // Arrange
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().toEpochMilli(), 4);
        // Act
        Event createdEvent = eventController.createEvent(eventCreateDto, TEST_USER_ID);

        // Assert
        assertTrue(createdEvent.getId() > 0);
        assertEquals(createdEvent.getName(), eventCreateDto.getName());
    }
    @Test
    void getEvents() {

       // Act
        List<Event> events = eventController.getEvents();
        List<Integer> eventIds = events.stream().map(Event::getId).toList();
        // Assert
        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertFalse(eventIds.contains(pastEventId));
    }
    @Test
    void getEventsIncludePast() {
        // Test for getEvents(boolean includePast) method with includePast as true
        // Act
        List<Event> events = eventController.getEvents(true);
        List<Integer> eventIds = events.stream().map(Event::getId).toList();

        // Assert
        assertNotNull(events);
        assertTrue(eventIds.contains(pastEventId));
        // Add more specific assertions if needed for past events inclusion
    }

    @Test
    void getOngoingEvents(){
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);
        eventController.joinEvent(TEST_USER_ID, event.getId());
        List<Event> ongoingEvents = eventController.getOngoingEvents(TEST_USER_ID);
        assertTrue(ongoingEvents.contains(event));
    }
    @Test
    void getUpcomingEvents() {
        // Arrange (Create some upcoming events for the user)
        EventCreateDto eventCreateDtoFuture = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDtoFuture, TEST_USER_ID);
        //Make sure the creator also joins the event
        eventController.joinEvent(TEST_USER_ID, event.getId());
        // Act
        List<Event> upcomingEvents = eventController.getUpComingEvents(TEST_USER_ID);
        List<Integer> eventIds = upcomingEvents.stream().map(Event::getId).toList();

        // Assert
        assertNotNull(upcomingEvents);
        assertTrue(eventIds.contains(event.getId()));
    }

    @Test
    void getEventById() {
        // Arrange
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().toEpochMilli(), 4);
        Event createdEvent = eventController.createEvent(eventCreateDto, TEST_USER_ID);

        // Act
        Event retrievedEvent = eventController.getEventById(createdEvent.getId());

        // Assert
        assertNotNull(retrievedEvent);
        assertEquals(createdEvent.getId(), retrievedEvent.getId());
    }
    @Test
    void getCreatedEventsById() {
        // Arrange (Create events for the user)
        EventCreateDto eventCreateDto1 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        EventCreateDto eventCreateDto2 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli(), 4);
        eventController.createEvent(eventCreateDto1, TEST_USER_ID);
        eventController.createEvent(eventCreateDto2, TEST_USER_ID);

        // Act
        List<Event> createdEvents = eventController.getCreatedEventsById(TEST_USER_ID);

        // Assert
        assertNotNull(createdEvents);
        assertTrue(createdEvents.size() > 2);
    }
    @Test
    void joinEvent() {
        // Arrange (Create an event)
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);

        // Act
        Event joinedEvent = eventController.joinEvent(TEST_USER_ID, event.getId());

        // Assert
        assertNotNull(joinedEvent);
        assertTrue(eventController.hasUserJoinedEvent(TEST_USER_ID, event.getId()));
    }
    @Test
    void leaveEvent() {
        // Arrange (Create an event and make the user join)
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);
        eventController.joinEvent(TEST_USER_ID, event.getId());

        // Act
        Event leftEvent = eventController.leaveEvent(TEST_USER_ID, event.getId());

        // Assert
        assertNotNull(leftEvent);
        assertFalse(eventController.hasUserJoinedEvent(TEST_USER_ID, event.getId()));
    }
    @Test
    void getEventsByUserId() {
        // Arrange (Create events for the user)
        EventCreateDto eventCreateDto1 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        EventCreateDto eventCreateDto2 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event e1 = eventController.createEvent(eventCreateDto1, TEST_USER_ID);
        Event e2 = eventController.createEvent(eventCreateDto2, TEST_USER_ID);
        //Join events
        eventController.joinEvent(TEST_USER_ID, e1.getId());
        eventController.joinEvent(TEST_USER_ID, e2.getId());
        // Act
        List<Event> userEvents = eventController.getEventsByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(userEvents);
        assertTrue(userEvents.contains(e1));
        assertTrue(userEvents.contains(e2));
    }
    @Test
    void cancelEvent() {
        // Arrange (Create an event)
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);

        // Act
        Event canceledEvent = eventController.cancelEvent(event.getId());

        // Assert
        assertNotNull(canceledEvent);
        assertTrue(canceledEvent.isCancelled());
    }
    @Test
    void editEvent() {
        // Arrange (Create an event)
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);

        // Edit the event
        EventCreateDto updatedEventDto = new EventCreateDto(TEST_USER_ID, "updated description", 10, 2, 2, Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli(), 6);
        Event editedEvent = eventController.editEvent(event.getId(), updatedEventDto);

        // Act
        Event retrievedEvent = eventController.getEventById(event.getId());

        // Assert
        assertNotNull(editedEvent);
        assertEquals("updated description", editedEvent.getDescription());
        assertEquals(10, editedEvent.getAmountOfPeople());
        assertEquals("updated description", retrievedEvent.getDescription());
        assertEquals(10, retrievedEvent.getAmountOfPeople());
    }
    @Test
    void getEventsFromSector() {
        // Arrange (Create events for a specific sector)
        int sectorNumber = 5;
        EventCreateDto eventCreateDto1 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, sectorNumber, Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        EventCreateDto eventCreateDto2 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, sectorNumber, Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event e1 = eventController.createEvent(eventCreateDto1, TEST_USER_ID);
        Event e2 = eventController.createEvent(eventCreateDto2, TEST_USER_ID);

        // Act
        List<Event> sectorEvents = eventController.getEventsFromSector(sectorNumber);

        // Assert
        assertNotNull(sectorEvents);
        assertFalse(sectorEvents.contains(e1));
        assertTrue(sectorEvents.contains(e2));
    }
    @Test
    void getEventsFromSectorIncludePast() {
        // Arrange (Create events for a specific sector)
        int sectorNumber = 7;
        EventCreateDto eventCreateDto1 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, sectorNumber, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        EventCreateDto eventCreateDto2 = new EventCreateDto(TEST_USER_ID, "description", 5, 1, sectorNumber, Instant.now().minus(2, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event e1 = eventController.createEvent(eventCreateDto1, TEST_USER_ID);
        Event e2 = eventController.createEvent(eventCreateDto2, TEST_USER_ID);

        // Act
        List<Event> sectorEvents = eventController.getEventsFromSector(sectorNumber, true);

        // Assert
        assertNotNull(sectorEvents);
        assertTrue(sectorEvents.contains(e1));
        assertTrue(sectorEvents.contains(e2));
    }
    @Test
    void addCoordinateToEvent() {
        // Arrange (Create an event)
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);

        // Act
        Coordinate coordinate = new Coordinate(10, 20);
        Event eventWithCoordinates = eventController.addCoordinateToEvent(event.getId(), coordinate);

        // Assert
        assertNotNull(eventWithCoordinates);
        assertNotNull(eventWithCoordinates.getEventLocation());
        assertEquals(10, eventWithCoordinates.getEventLocation().x());
        assertEquals(20, eventWithCoordinates.getEventLocation().y());
    }
    @Test
    void hasUserJoinedEvent() {
        // Arrange (Create an event and make the user join)
        EventCreateDto eventCreateDto = new EventCreateDto(TEST_USER_ID, "description", 5, 1, 1, Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli(), 4);
        Event event = eventController.createEvent(eventCreateDto, TEST_USER_ID);
        eventController.joinEvent(TEST_USER_ID, event.getId());

        // Act
        boolean hasJoined = eventController.hasUserJoinedEvent(TEST_USER_ID, event.getId());
        boolean hasJoined2 = eventController.hasUserJoinedEvent(TEST_USER_ID + "2", event.getId());
        // Assert
        assertTrue(hasJoined);
        assertFalse(hasJoined2);
    }
}
