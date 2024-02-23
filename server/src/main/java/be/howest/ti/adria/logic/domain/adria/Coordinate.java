package be.howest.ti.adria.logic.domain.adria;

import java.util.Objects;

public record Coordinate(int x, int y) {
    public static Coordinate empty() {
        return new Coordinate(-1,-1);
    }
    public boolean isEmpty(){
        return this.x < 0 || this.y < 0;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}