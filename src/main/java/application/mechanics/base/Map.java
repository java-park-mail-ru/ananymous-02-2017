package application.mechanics.base;

@SuppressWarnings({"ClassNamingConvention", "PublicField", "StaticVariableNamingConvention"})
public final class Map {
    public static int[][] map = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 0, 0, 0, 0, 0, 1, 1, 1},
        {1, 1, 0, 0, 2, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 2, 0, 0, 0, 1},
        {1, 0, 0, 2, 0, 0, 2, 0, 0, 1},
        {1, 0, 0, 0, 2, 0, 0, 0, 1, 1},
        {1, 1, 1, 0, 0, 0, 0, 1, 1, 1},
        {1, 1, 1, 0, 0, 1, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 1, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    public static int m = map.length;
    public static int n = map[0].length;
}
