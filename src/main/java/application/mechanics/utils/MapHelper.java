package application.mechanics.utils;

import application.mechanics.Config;
import application.mechanics.base.Map;
import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;

public class MapHelper {
    public static final double X_OFFSET = (Map.M / 2) * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;
    public static final double Z_OFFSET = (Map.N / 2) * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;

    @NotNull
    public static Index getIndex(@NotNull Coordinates point) {
        final Index index = new Index();
        index.i = (int) (point.x / Config.BLOCK_SIZE + 1. / 2) + Map.M / 2;
        index.j = (int) (point.z / Config.BLOCK_SIZE + 1. / 2) + Map.N / 2;
        return index;
    }
}
