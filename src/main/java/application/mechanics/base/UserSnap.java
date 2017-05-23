package application.mechanics.base;

import org.jetbrains.annotations.NotNull;

public class UserSnap {
    @NotNull
    private Long id;
    @NotNull
    private Coordinates position;
    @NotNull
    private Coordinates camera;
    private boolean firing;

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

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

}
