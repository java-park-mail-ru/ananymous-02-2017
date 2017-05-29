package application.mechanics.base;

import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;

public class MyVector {
    private Coordinates coords;

    // TODO check
    public MyVector() {
    }

    public MyVector(@NotNull Coordinates coords) {
        this.coords = coords;
    }

    public MyVector(double x, double y, double z) {
        this.coords = new Coordinates(x, y, z);
    }

    public double getCos(@NotNull MyVector other) {
        return getScalarMultipl(other) / (getModule() * other.getModule());
    }

    public double getScalarMultipl(@NotNull MyVector other) {
        return this.coords.x * other.getX() + this.coords.y * other.getY() + this.coords.z * other.getZ();
    }

    public double getModule() {
        final double sum = this.coords.x*this.coords.x + this.coords.y*this.coords.y + this.coords.z*this.coords.z;
        return Math.sqrt(sum);
    }

    @NotNull
    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(@NotNull Coordinates coords) {
        this.coords = coords;
    }

    public double getX() {
        return coords.x;
    }
    public double getY() {
        return coords.y;
    }
    public double getZ() {
        return coords.z;
    }
}
