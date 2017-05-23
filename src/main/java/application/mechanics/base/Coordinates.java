package application.mechanics.base;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"InstanceVariableNamingConvention", "PublicField"})
public class Coordinates {
    public double x;
    public double y;
    public double z;

    // TODO check for needed
    public Coordinates() {
    }

    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public Coordinates add(@NotNull Coordinates addition) {
        return new Coordinates(x + addition.x, y + addition.y, z + addition.z);
    }

    @NotNull
    public Coordinates subtract(@NotNull Coordinates subtractor) {
        return new Coordinates(x - subtractor.x, y - subtractor.y, z - subtractor.z);
    }

    public double getDistanceBetween(@NotNull Coordinates other) {
        final double dx = (this.x - other.x) * (this.x - other.x);
        final double dy = (this.y - other.y) * (this.y - other.y);
        final double dz = (this.z - other.z) * (this.z - other.z);
        return Math.sqrt(dx + dy + dz);
    }
}
