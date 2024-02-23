package be.howest.ti.adria.web.dto;

import be.howest.ti.adria.logic.domain.adria.DangerousArea;
import be.howest.ti.adria.logic.domain.adria.Sector;
import be.howest.ti.adria.web.dto.adria.DangerousAreaDto;

public class DangerZoneInfoDto{
    private final Sector sector;
    private final DangerousAreaDto dangerousArea;
    public DangerZoneInfoDto(Sector sector, DangerousArea dangerousArea) {
        this.sector = sector;
        this.dangerousArea = dangerousArea.toDto();
    }
    public DangerZoneInfoDto(Sector sector, DangerousAreaDto dangerousArea) {
        this.sector = sector;
        this.dangerousArea = dangerousArea;
    }

    public Sector getSector() {
        return sector;
    }

    public DangerousAreaDto getDangerousArea() {
        return dangerousArea;
    }
}
