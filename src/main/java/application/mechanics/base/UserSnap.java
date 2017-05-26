package application.mechanics.base;

import org.jetbrains.annotations.NotNull;

public class UserSnap {
    private long id = -1;
    @NotNull
    private Coordinates position;
    @NotNull
    private Coordinates camera;
    private boolean shooting;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(@NotNull Coordinates position) {
        this.position = position;
    }

    @NotNull
    public Coordinates getCamera() {
        return camera;
    }

    public void setCamera(@NotNull Coordinates camera) {
        this.camera = camera;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    @Override
    public String toString() {
        return "{id:" + id + ",position:" + position.toString() + ",camera:" + camera.toString() + ",shooting:" + shooting + '}';
    }
}
