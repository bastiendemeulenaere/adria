package be.howest.ti.adria.web.dto;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.Sector;

public class LocationInfoDto {
    private final Sector sector;
    private final Coordinate coordinate;

    public Sector getSector() {
        return sector;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public LocationInfoDto(Sector sector, Coordinate coordinate) {
        this.sector = sector;
        this.coordinate = coordinate;
    }
}
