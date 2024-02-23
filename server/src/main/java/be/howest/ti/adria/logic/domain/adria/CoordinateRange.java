package be.howest.ti.adria.logic.domain.adria;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public record CoordinateRange(Coordinate start, Coordinate end) {
    private static final Random random = new SecureRandom();

    public boolean containsCoordinate(Coordinate coordinate) {
        int x = coordinate.x();
        int y = coordinate.y();

        int startX = start.x();
        int startY = start.y();
        int endX = end.x();
        int endY = end.y();

        return x >= startX && x <= endX && y >= startY && y <= endY;
    }
    public Coordinate getRandomPoint() {
        int rangeX = end.x() - start.x() + 1;
        int rangeY = end.y() - start.y() + 1;

        int randomX = start.x() + random.nextInt(rangeX);
        int randomY = start.y() + random.nextInt(rangeY);

        return new Coordinate(randomX, randomY);
    }
    @Override
    public String toString() {
        return String.format("[%s to %s]", start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateRange that = (CoordinateRange) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
