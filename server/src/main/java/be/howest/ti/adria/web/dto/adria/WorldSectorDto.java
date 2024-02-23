package be.howest.ti.adria.web.dto.adria;

import be.howest.ti.adria.logic.domain.adria.CoordinateRange;

import java.util.List;

public class WorldSectorDto {
    private final String name;
    private final CoordinateRange coordinateRange;
    private final List<DangerousAreaDto> dangerousAreas;

    public String getName() {
        return name;
    }

    public CoordinateRange getCoordinateRange() {
        return coordinateRange;
    }

    public List<DangerousAreaDto> getDangerousAreas() {
        return dangerousAreas;
    }

    public WorldSectorDto(String name, CoordinateRange coordinateRange, List<DangerousAreaDto> dangerousAreas) {
        this.name = name;
        this.coordinateRange = coordinateRange;
        this.dangerousAreas = dangerousAreas;
    }

}
