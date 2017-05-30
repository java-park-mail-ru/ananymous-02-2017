package application.mechanics.internal;

import application.mechanics.Config;
import application.mechanics.base.Block;
import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@SuppressWarnings("InstanceVariableNamingConvention")
@Service
public class BlockService {
    final int m;
    final int n;
    final Block[][] blocks;

    public BlockService() {
        final int[][] map = {
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
        m = map.length;
        n = map[0].length;
        blocks = new Block[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] > 0) {
                    final Coordinates corner = new Coordinates(
                            i * Config.BLOCK_SIZE,
                            Config.BLOCK_HEIGHT,
                            j * Config.BLOCK_SIZE
                    );
                    final Block block = new Block(corner, Config.BLOCK_SIZE, Config.BLOCK_HEIGHT, Config.BLOCK_SIZE);
                    blocks[i][j] = block;
                }
            }
        }
    }

    public boolean isWallsBetween(@NotNull Coordinates from, @NotNull Coordinates to) {
        final Index fromIndex = getIndexOnMap(from);
        final Index toIndex = getIndexOnMap(to);
        for (int i = fromIndex.i; i <= toIndex.i; i++) {
            for (int j = fromIndex.j; j < toIndex.j; j++) {
                if (blocks[i][j] == null) {
                    continue;
                }
                if (blocks[i][j].isBetween(from, to)) {
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    private Index getIndexOnMap(@NotNull Coordinates point) {
        final Index index = new Index();
        index.j = (int) (point.x / Config.BLOCK_SIZE);
        index.i = (int) (point.z / Config.BLOCK_SIZE);
        return index;
    }

    @SuppressWarnings("PublicField")
    private static class Index {
        public int i;
        public int j;
    }
}
