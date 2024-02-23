package be.howest.ti.adria.web.dto;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.Sector;

public record SuggestedLocationDto(Sector sector, Coordinate coordinate) {
}
