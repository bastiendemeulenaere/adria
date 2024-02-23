package be.howest.ti.adria.logic.domain;

import be.howest.ti.adria.logic.controller.EventController;
import be.howest.ti.adria.logic.domain.adria.*;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.listeners.AdriaUserListener;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SafeZoneFinder {
    private final World adriaWorld;
    private final EventController eventController;
    private final Map<String, AdriaUserListener> adriaUserListeners = new HashMap<>();
    private final ScheduledExecutorService scheduler;

    public SafeZoneFinder(World adriaWorld, EventController eventController) {
        this.adriaWorld = adriaWorld;
        this.eventController = eventController;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::tick, 1, 1, TimeUnit.SECONDS);

    }

    private void tick(){
        checkEventTimes();
    }
    private void checkEventTimes(){
        List<Event> events = eventController.getEvents();
        for (Event event : events) {
            if (!event.hasLocation() && !event.isCancelled()){
                checkEventTime(event);
            }
        }
    }
    private void checkEventTime(Event event) {
        Instant startTime = event.getStartDateTime();
        Instant now = Instant.now();

        // check if startTime is less than one hour from now
        if (startTime.isBefore(now.plus(1, ChronoUnit.HOURS))) {
            Sector sector = event.getSector();
            notifySuggestLocation(event.getOrganiser().getId(), sector, findSafeLocation(sector), event);
        }
    }

    public void addLocationListener(String adriaId, AdriaUserListener adriaUserListener) {
        this.adriaUserListeners.put(adriaId, adriaUserListener);
    }
    private void notifySuggestLocation(String adriaId, Sector sector, Coordinate coordinate, Event event){
        AdriaUserListener adriaUserListener = adriaUserListeners.get(adriaId);
        if (adriaUserListener == null) { //if user is not online, don't send the request
            return;
        }
        adriaUserListener.notifySuggestLocation(sector, coordinate, event);
    }

    private Coordinate findSafeLocation(Sector sector){
        WorldSector worldSector = adriaWorld.getSector(sector);
        CoordinateRange coordinateRange = worldSector.getCoordinateRange();
        Coordinate point = coordinateRange.getRandomPoint();
        while (!worldSector.isPointSafe(point)){
            point = coordinateRange.getRandomPoint();
        }
        return point;
    }
}
