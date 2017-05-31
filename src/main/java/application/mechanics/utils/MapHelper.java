package application.mechanics.utils;

import application.mechanics.Config;
import application.mechanics.base.Map;

public class MapHelper {
    public static final double X_OFFSET = Map.M * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;
    public static final double Z_OFFSET = Map.N * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;
}
