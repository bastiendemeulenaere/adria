package be.howest.ti.adria.web.dto.adria;

import java.util.List;

public class WorldDto {
    private final List<WorldSectorDto> sectors;

    public List<WorldSectorDto> getSectors() {
        return sectors;
    }

    public WorldDto(List<WorldSectorDto> sectors){
        this.sectors = sectors;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (WorldSectorDto sector : sectors) {
            sb.append("WorldSector: ").append(sector.getName()).append("\n");
            sb.append("WorldSector Coordinates: ").append(sector.getCoordinateRange().start()).append(" to ").append(sector.getCoordinateRange().end()).append("\n");
            sb.append("Dangerous Areas:\n");
            for (DangerousAreaDto dangerousArea : sector.getDangerousAreas()) {
                sb.append("- ").append(dangerousArea.getReason()).append(" Coordinates: ").append(dangerousArea.getCoordinateRange().start()).append(" to ").append(dangerousArea.getCoordinateRange().end()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
