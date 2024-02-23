package be.howest.ti.adria.logic.domain;

import be.howest.ti.adria.logic.controller.EventController;
import be.howest.ti.adria.logic.domain.adria.*;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.listeners.AdriaUserListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiskObserver {
    private final Map<String, AdriaUserListener> dangerListeners = new HashMap<>();
    private final EventController eventController;
    public RiskObserver(World adriaWorld, EventController eventController) {
        this.eventController = eventController;
        adriaWorld.addDangerZoneListener(this::onDangerzoneUpdate);
    }
    private void onDangerzoneUpdate(Sector sector, DangerousArea dangerousArea){
        List<Event> eventsInSector = eventController.getEventsFromSector(sector.getSectorNumber(), true);
        for (Event event : eventsInSector) {
            if (event.hasLocation() && !event.isCancelled() && event.isOngoing()){
                Coordinate eventLocation = event.getEventLocation();
                CoordinateRange dangerousAreaRange = dangerousArea.coordinateRange();
                if (dangerousAreaRange.containsCoordinate(eventLocation)){
                    notifyAttendeesOfDangerzone(getAttendingAdriaIds(event, true), event,dangerousArea);
                    cancelEvent(event);
                    return;
                }
            }
        }
    }
    private void cancelEvent(Event event){
        eventController.cancelEvent(event.getId());
    }
    private void notifyAttendeesOfDangerzone(List<String> attendees, Event event,DangerousArea dangerousArea) {
        for (Map.Entry<String, AdriaUserListener> entry : dangerListeners.entrySet()) {
            if (attendees.contains(entry.getKey())){
                entry.getValue().notifyDanger(event, dangerousArea);
            }
        }
    }

    public void addDangerListener(String adriaId, AdriaUserListener dangerListener) {
        this.dangerListeners.put(adriaId, dangerListener);
    }
    private List<String> getAttendingAdriaIds(Event event, boolean includeOrganiser){
        return event
                .getAttendees()
                .stream()
                .map(User::getId)
                .filter(attendee -> includeOrganiser || !attendee.equals(event.getOrganiser().getId())) // don't include the organiser
                .toList();
    }
}
