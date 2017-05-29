package application.mechanics.base;

import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Block {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class.getSimpleName());

    @NotNull
    private Coordinates center;
    private double xLength;
    private double yLength;
    private double zLength;

    private double xRadius;
    private double yRadius;
    private double zRadius;

    // TODO remove if can
    public Block(@NotNull Coordinates center) {
        this.center = center;
    }

    public Block(@NotNull Coordinates center, double xLength, double yLength, double zLength) {
        this.center = center;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        xRadius = xLength / 2;
        yRadius = yLength / 2;
        zRadius = zLength / 2;
    }

    // TODO remove getters and setters
    @NotNull
    public Coordinates getCenter() {
        return center;
    }

    public void setCenter(@NotNull Coordinates center) {
        this.center = center;
    }

    public double getxLength() {
        return xLength;
    }

    public void setxLength(double xLength) {
        this.xLength = xLength;
        xRadius = xLength / 2;
    }

    public double getyLength() {
        return yLength;
    }

    public void setyLength(double yLength) {
        this.yLength = yLength;
        yRadius = yLength / 2;
    }

    public double getzLength() {
        return zLength;
    }

    public void setzLength(double zLength) {
        this.zLength = zLength;
        zRadius = zLength / 2;
    }

    public double getxRadius() {
        return xRadius;
    }

    public double getyRadius() {
        return yRadius;
    }

    public double getzRadius() {
        return zRadius;
    }

    private double getClosestXPlane(@NotNull Coordinates point) {
        final double first = center.x + xRadius;
        final double second = center.x - xRadius;
        if (Math.abs(point.x - first) < Math.abs(point.x - second)) {
            return first;
        }
        else {
            return second;
        }
    }

    private double getYPlane() {
        return center.y + yRadius;
    }

    private double getClosestZPlane(@NotNull Coordinates point) {
        final double first = center.z + zRadius;
        final double second = center.z - zRadius;
        if (Math.abs(point.z - first) < Math.abs(point.z - second)) {
            return first;
        }
        else {
            return second;
        }
    }

    @Nullable
    private Coordinates getIntersectionPointWithX(@NotNull Ray ray) {
        final MyVector vector = ray.getVector();
        final Coordinates linePoint = ray.getPoint();
        final double plane = getClosestXPlane(ray.getPoint());
        final double t = (plane - linePoint.x) / vector.getX();

        final double x = plane;
        final double y = vector.getY() * t + linePoint.y;
        final double z = vector.getZ() * t + linePoint.z;

        if (vector.getX() >= 0 && x < linePoint.x || vector.getX() < 0 && x > linePoint.x) {
            return null;
        }

        return new Coordinates(x, y, z);
    }

    @NotNull
    private Coordinates getIntersectionPointWithY(Ray ray) {
        final MyVector vector = ray.getVector();
        final Coordinates linePoint = ray.getPoint();
        final double plane = getYPlane();
        final double t = (plane - linePoint.y) / vector.getY();

        final double y = plane;
        final double x = vector.getX() * t + linePoint.x;
        final double z = vector.getZ() * t + linePoint.z;
        return new Coordinates(x, y, z);
    }

    @Nullable
    private Coordinates getIntersectionPointWithZ(Ray ray) {
        final MyVector vector = ray.getVector();
        final Coordinates linePoint = ray.getPoint();
        final double plane = getClosestZPlane(ray.getPoint());
        final double t = (plane - linePoint.z) / vector.getZ();

        final double z = plane;
        final double y = vector.getY() * t + linePoint.y;
        final double x = vector.getX() * t + linePoint.x;

        if (vector.getZ() >= 0 && z < linePoint.z || vector.getZ() < 0 && z > linePoint.z) {
            return null;
        }

        return new Coordinates(x, y, z);
    }

    private boolean isInside (Coordinates point) {
        if (point == null) {
            return false;
        }
        boolean xOK = point.x >= center.x - xRadius && point.x <= center.x + xRadius;
        boolean yOK = point.y >= center.y - yRadius && point.y <= center.y + yRadius;
        boolean zOK = point.z >= center.z - zRadius && point.z <= center.z + zRadius;
        return xOK && yOK && zOK;
    }

    @Nullable
    public Double isOnTheWay (Ray ray) {
        Coordinates intersectionX = getIntersectionPointWithX(ray);
        if (intersectionX != null) {
            LOGGER.info("X point: ({}, {}, {})", intersectionX.x, intersectionX.y, intersectionX.z);
        }
        Coordinates intersectionY = getIntersectionPointWithY(ray);
        Coordinates intersectionZ = getIntersectionPointWithZ(ray);
        boolean xInside = isInside(intersectionX);
        boolean yInside = isInside(intersectionY);
        boolean zInside = isInside(intersectionZ);

        if (!(xInside || yInside || zInside)) {
            return null;
        }
        double min = 100000;
        if (intersectionX != null && xInside && intersectionX.getDistanceBetween(ray.getPoint()) < min) {
            min = intersectionX.getDistanceBetween(ray.getPoint());
        }

        if (intersectionY != null && yInside && intersectionY.getDistanceBetween(ray.getPoint()) < min) {
            min = intersectionY.getDistanceBetween(ray.getPoint());
        }

        if (intersectionZ != null && zInside && intersectionZ.getDistanceBetween(ray.getPoint()) < min) {
            min = intersectionZ.getDistanceBetween(ray.getPoint());
        }
        return min;
    }
}
