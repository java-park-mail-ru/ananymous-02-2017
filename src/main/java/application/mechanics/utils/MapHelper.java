package application.mechanics.utils;

import application.mechanics.Config;
import application.mechanics.base.Map;

public class MapHelper {
    public static final double X_OFFSET = (Map.M / 2) * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;
    public static final double Z_OFFSET = (Map.N / 2) * Config.BLOCK_SIZE + Config.BLOCK_SIZE / 2;
}
