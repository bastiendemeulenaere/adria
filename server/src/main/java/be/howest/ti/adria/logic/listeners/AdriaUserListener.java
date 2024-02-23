package be.howest.ti.adria.logic.listeners;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.DangerousArea;
import be.howest.ti.adria.logic.domain.adria.Sector;
import be.howest.ti.adria.logic.domain.event.Event;

public interface AdriaUserListener{
    void notifySuggestLocation(Sector sector, Coordinate coordinate, Event event);
    void notifyLocation(Coordinate coordinate, Event event);
    void notifyDanger(Event event, DangerousArea dangerousArea);
    void notifyCanceled(Event event);
}

