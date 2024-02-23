package be.howest.ti.adria.logic.listeners;

import be.howest.ti.adria.logic.domain.adria.DangerousArea;
import be.howest.ti.adria.logic.domain.adria.Sector;

public interface DangerZoneListener {
    void notifyDangerZone(Sector sector, DangerousArea dangerousArea);
}
