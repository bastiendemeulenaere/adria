package be.howest.ti.adria.logic.domain.adria;

import be.howest.ti.adria.logic.listeners.UpdateListener;
import be.howest.ti.adria.logic.listeners.DangerZoneListener;
import be.howest.ti.adria.web.dto.adria.WorldDto;

import java.util.*;

public class World {
    private static final int SIZE = 100;
    private Map<Sector, WorldSector> sectors = new EnumMap<>(Sector.class);
    private final List<UpdateListener> sectorUpdateListeners = new ArrayList<>();
    private final List<DangerZoneListener> dangerZoneListeners = new ArrayList<>();

    private void notifyListeners(){
        sectorUpdateListeners.forEach(UpdateListener::onUpdate);
    }
    private void notifyDangerZoneListeners(Sector sector, DangerousArea dangerousArea){
        dangerZoneListeners.forEach(adriaUserListener -> adriaUserListener.notifyDangerZone(sector, dangerousArea));
    }
    public void addUpdateListener(UpdateListener updateListener) {
        sectorUpdateListeners.add(updateListener);
    }
    public void addDangerZoneListener(DangerZoneListener adriaUserListener) {
        dangerZoneListeners.add(adriaUserListener);
    }
    public World(){
        this.sectors = generateSectors();
    }

    public void addDangerousZoneToSector(Sector sector, DangerousArea dangerousArea) {
        this.sectors.get(sector).addDangerousArea(dangerousArea);
        notifyListeners();
        notifyDangerZoneListeners(sector, dangerousArea);
    }


    private Map<Sector, WorldSector> generateSectors() {
        Map<Sector, WorldSector> allSectors = new EnumMap<>(Sector.class);
        int numSectors = Sector.values().length;

        int gridSize = (int) Math.ceil(Math.sqrt(numSectors));
        int sectorSize = SIZE / gridSize;

        int sectorNumber = 1;
        for (Sector sector : Sector.values()) {
            if (sectorNumber > numSectors) {
                break;
            }

            CoordinateRange sectorCoordinateRange = getCoordinateRange(sectorNumber, gridSize, sectorSize);

            String sectorName = sector.getSectorName();
            allSectors.put(sector, new WorldSector(sectorName, sectorCoordinateRange));

            sectorNumber++;
        }

        return allSectors;
    }

    private static CoordinateRange getCoordinateRange(int sectorNumber, int gridSize, int sectorSize) {
        int row = (sectorNumber - 1) / gridSize;
        int col = (sectorNumber - 1) % gridSize;

        int sectorStartX = col * sectorSize;
        int sectorStartY = row * sectorSize;
        int sectorEndX = sectorStartX + sectorSize;
        int sectorEndY = sectorStartY + sectorSize;

        return new CoordinateRange(
                new Coordinate(sectorStartX, sectorStartY),
                new Coordinate(sectorEndX, sectorEndY)
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (WorldSector sector : sectors.values()) {
            sb.append("WorldSector: ").append(sector.getName()).append("\n");
            sb.append("WorldSector Coordinates: ").append(sector.getCoordinateRange().start()).append(" to ").append(sector.getCoordinateRange().end()).append("\n");
            sb.append("Dangerous Areas:\n");
            for (DangerousArea dangerousArea : sector.getDangerousAreas()) {
                sb.append("- ").append(dangerousArea.reason()).append(" Coordinates: ").append(dangerousArea.coordinateRange().start()).append(" to ").append(dangerousArea.coordinateRange().end()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public List<WorldSector> getWorldSectors() {
        return sectors.values().stream().toList();
    }
    public List<Sector> getSectors(){
        return sectors.keySet().stream().toList();
    }

    public void removeDangerousZonesInSector(WorldSector sector) {
        sector.removeDangerousAreas();
        notifyListeners();
    }

    public WorldDto toDto() {
        return new WorldDto(sectors.values().stream().map(WorldSector::toDto).toList());
    }

    public WorldSector getSector(Sector sectorByNumber) {
        return sectors.get(sectorByNumber);
    }
}
