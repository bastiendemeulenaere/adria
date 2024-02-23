package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.RiskObserver;
import be.howest.ti.adria.logic.domain.SafeZoneFinder;
import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.adria.*;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.listeners.AdriaUserListener;
import be.howest.ti.adria.logic.listeners.WeatherListener;
import be.howest.ti.adria.web.listeners.NotificationListener;
import be.howest.ti.adria.web.socketevents.ClientUser;
import be.howest.ti.adria.web.socketevents.IncomingSocketEvent;
import be.howest.ti.adria.web.socketevents.SocketEventType;
import be.howest.ti.adria.web.socketevents.SocketJoinEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdriaController {
    private final World world;
    private final RiskObserver riskObserver;
    private final SafeZoneFinder safeZoneFinder;
    private final Set<ClientUser> connectedClients = new HashSet<>();
    private final NotificationListener notificationListener;
    private static final Logger LOGGER = Logger.getLogger(AdriaController.class.getName());
    public AdriaController(World world, RiskObserver riskObserver, SafeZoneFinder safeZoneFinder, NotificationListener notificationListener) {
        this.world = world;
        this.riskObserver = riskObserver;
        this.safeZoneFinder = safeZoneFinder;
        this.notificationListener = notificationListener;
    }

    public void subscribeToWeather(WeatherListener listener){
        world.addUpdateListener(() -> listener.onWeatherChange(world));
    }
    public void subscribeToLocations(String adriaId, AdriaUserListener adriaUserListener){
        safeZoneFinder.addLocationListener(adriaId, adriaUserListener);
    }

    public void handleSocket(IncomingSocketEvent e) {
        if (Objects.requireNonNull(e.getEventType()) == SocketEventType.JOIN) {
            handleJoinEvent((SocketJoinEvent) e);
        }
    }
    public void handleJoinEvent(SocketJoinEvent e){
        ClientUser clientUser = new ClientUser(e.getAdriaId(), notificationListener);
        connectedClients.add(clientUser);
        subscribeToLocations(e.getAdriaId(), clientUser);
        riskObserver.addDangerListener(e.getAdriaId(), clientUser);
    }

    public void removeListenersOf(String adriaId) {
        connectedClients.removeIf(clientUser -> clientUser.getAdriaId().equals(adriaId));
    }

    public void sendNotifyLocation(Event event, Coordinate coordinate) {
        List<String> attendingAdriaIds = getAttendingAdriaIds(event, false);
        for (ClientUser clientUser : connectedClients) {
            if (attendingAdriaIds.contains(clientUser.getAdriaId()))
            {
                clientUser.notifyLocation(coordinate, event);
            }
        }
    }
    public void sendCancelNotification(Event event){
        List<String> attendingAdriaIds = getAttendingAdriaIds(event, false);
        for (ClientUser clientUser : connectedClients) {
            if (attendingAdriaIds.contains(clientUser.getAdriaId()))
            {
                clientUser.notifyCanceled(event);
            }
        }
    }
    private List<String> getAttendingAdriaIds(Event event, boolean includeOrganiser){
        User organiser = event.getOrganiser();
        if (organiser == null){
            LOGGER.log(Level.SEVERE, "Organiser for event {0} not found!", event.getId());
        }
        return event
                .getAttendees()
                .stream()
                .map(User::getId)
                .filter(attendee -> includeOrganiser || (organiser != null && !attendee.equals(organiser.getId()))) // don't include the organiser
                .toList();
    }

    public DangerousArea addDangerzone(Sector sector) {
        DangerousArea dangerousArea = new DangerousArea(
                "Firestorm",
                world.getSector(sector).getCoordinateRange(),
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.HOURS)
        );
        world.addDangerousZoneToSector(sector, dangerousArea);
        return dangerousArea;
    }
}
