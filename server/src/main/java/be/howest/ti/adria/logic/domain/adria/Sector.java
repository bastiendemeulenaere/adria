package be.howest.ti.adria.logic.domain.adria;

import be.howest.ti.adria.web.dto.adria.SectorDto;

import java.util.HashMap;
import java.util.Map;

public enum Sector {
    LUMINARA(1, "Luminara"),
    MIRAGEA(2, "Miragea"),
    EVERDAWN(3, "Everdawn"),
    ABYSSIA(4, "Abyssia"),
    CELESTIA(5, "Celestia"),
    VITALIS(6, "Vitalis"),
    NEBULIA(7, "Nebulia"),
    RADIANTIA(8, "Radiantia"),
    SERENARA(9, "Serenara");

    private final int sectorNumber;
    private final String sectorName;
    private static final Map<Integer, Sector> sectorMap = new HashMap<>();

    static {
        for (Sector sector : Sector.values()) {
            sectorMap.put(sector.sectorNumber, sector);
        }
    }

    Sector(int sectorNumber, String sectorName) {
        this.sectorNumber = sectorNumber;
        this.sectorName = sectorName;
    }

    public int getSectorNumber() {
        return sectorNumber;
    }

    public String getSectorName() {
        return sectorName;
    }

    public static Sector from(int number) {
        return sectorMap.get(number);
    }
    public SectorDto toDto(){
        return new SectorDto(sectorNumber, sectorName);
    }
}
