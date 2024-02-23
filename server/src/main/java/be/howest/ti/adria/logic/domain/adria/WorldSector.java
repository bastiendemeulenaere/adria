package be.howest.ti.adria.logic.domain.adria;

import be.howest.ti.adria.web.dto.adria.WorldSectorDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldSector {
    private final String name;
    private final CoordinateRange coordinateRange;
    private final List<DangerousArea> dangerousAreas;

    public WorldSector(String name, CoordinateRange coordinateRange) {
        this.name = name;
        this.coordinateRange = coordinateRange;
        this.dangerousAreas = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public CoordinateRange getCoordinateRange() {
        return coordinateRange;
    }

    public List<DangerousArea> getDangerousAreas() {
        return dangerousAreas;
    }

    public void addDangerousArea(DangerousArea dangerousArea) {
        this.dangerousAreas.add(dangerousArea);
    }
    public void addDangerousAreas(List<DangerousArea> dangerousAreas) {
        this.dangerousAreas.addAll(dangerousAreas);
    }
    public boolean containsCoordinate(Coordinate coordinate) {
        return coordinateRange.containsCoordinate(coordinate);
    }
    public void removeDangerousArea(int i) {
        DangerousArea dangerousArea = this.dangerousAreas.get(i);
        this.dangerousAreas.remove(dangerousArea);
    }
    public void removeDangerousAreas() {
        this.dangerousAreas.clear();
    }
    public boolean isPointSafe(Coordinate point) {
        for (DangerousArea dangerousArea : dangerousAreas) {
            if (dangerousArea.coordinateRange().containsCoordinate(point)) return false;
        }
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldSector sector = (WorldSector) o;
        return Objects.equals(name, sector.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public WorldSectorDto toDto() {
        return new WorldSectorDto(name, coordinateRange, dangerousAreas.stream().map(DangerousArea::toDto).toList());
    }


}