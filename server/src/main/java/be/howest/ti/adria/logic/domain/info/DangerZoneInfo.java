package be.howest.ti.adria.logic.domain.info;

import be.howest.ti.adria.logic.domain.adria.DangerousArea;
import be.howest.ti.adria.logic.domain.adria.Sector;

public record DangerZoneInfo(Sector sector, DangerousArea dangerousArea) {

}
