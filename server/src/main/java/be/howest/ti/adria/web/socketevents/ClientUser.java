package be.howest.ti.adria.web.socketevents;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.DangerousArea;
import be.howest.ti.adria.logic.domain.adria.Sector;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.listeners.AdriaUserListener;
import be.howest.ti.adria.logic.listeners.DangerZoneListener;
import be.howest.ti.adria.web.dto.DangerZoneInfoDto;
import be.howest.ti.adria.web.dto.SuggestedLocationDto;
import be.howest.ti.adria.web.listeners.NotificationListener;

import java.util.Objects;

public class ClientUser implements AdriaUserListener, DangerZoneListener {
    private final String adriaId;
    private final NotificationListener notificationListener;

    public String getAdriaId() {
        return adriaId;
    }

    public ClientUser(String adriaId, NotificationListener notificationListener) {
        this.adriaId = adriaId;
        this.notificationListener = notificationListener;
    }

    @Override
    public void notifyDangerZone(Sector sector, DangerousArea dangerousArea) {
        notificationListener.sendDangerZoneNotification(adriaId, new DangerZoneInfoDto(sector, dangerousArea.toDto()));
    }

    @Override
    public void notifySuggestLocation(Sector sector, Coordinate coordinate, Event event) {
        notificationListener.sendSuggestLocationNotification(adriaId, new SuggestedLocationDto(sector, coordinate), event.toDto());
    }

    @Override
    public void notifyLocation(Coordinate coordinate, Event event) {
        notificationListener.sendLocationNotification(adriaId, coordinate, event.toDto());
    }

    @Override
    public void notifyDanger(Event event, DangerousArea dangerousArea) {
        notificationListener.sendDangerNotification(adriaId, event.toDto(), dangerousArea.toDto());
    }

    @Override
    public void notifyCanceled(Event event) {
        notificationListener.sendCanceledNotification(adriaId, event.toDto());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientUser that = (ClientUser) o;
        return Objects.equals(adriaId, that.adriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adriaId);
    }
}
