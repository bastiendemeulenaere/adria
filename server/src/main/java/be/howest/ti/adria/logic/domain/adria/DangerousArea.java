package be.howest.ti.adria.logic.domain.adria;

import be.howest.ti.adria.web.dto.adria.DangerousAreaDto;

import java.time.Instant;
import java.util.Objects;

public record DangerousArea(String reason, CoordinateRange coordinateRange, Instant startTime, Instant endTime) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DangerousArea that = (DangerousArea) o;
        return Objects.equals(reason, that.reason) && Objects.equals(coordinateRange, that.coordinateRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, coordinateRange);
    }

    public DangerousAreaDto toDto() {
        return new DangerousAreaDto(reason, coordinateRange, startTime, endTime);
    }
}