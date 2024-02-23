package be.howest.ti.adria.web.dto.adria;

import be.howest.ti.adria.logic.domain.adria.CoordinateRange;

import java.time.Instant;
import java.util.Objects;

public class DangerousAreaDto {
    private final String reason;
    private final CoordinateRange coordinateRange;
    private final long startTime;
    private final long endTime;

    public DangerousAreaDto(String reason, CoordinateRange coordinateRange, Instant startTime, Instant endTime) {
        this.reason = reason;
        this.coordinateRange = coordinateRange;
        this.startTime = startTime.toEpochMilli();
        this.endTime = endTime.toEpochMilli();
    }

    public String getReason() {
        return reason;
    }

    public CoordinateRange getCoordinateRange() {
        return coordinateRange;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DangerousAreaDto) obj;
        return Objects.equals(this.reason, that.reason) &&
                Objects.equals(this.coordinateRange, that.coordinateRange) &&
                Objects.equals(this.startTime, that.startTime) &&
                Objects.equals(this.endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, coordinateRange, startTime, endTime);
    }

    @Override
    public String toString() {
        return "DangerousAreaDto[" +
                "reason=" + reason + ", " +
                "coordinateRange=" + coordinateRange + ", " +
                "startTime=" + startTime + ", " +
                "endTime=" + endTime + ']';
    }


}
