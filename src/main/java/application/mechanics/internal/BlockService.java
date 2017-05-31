package application.mechanics.internal;

import application.mechanics.Config;
import application.mechanics.base.Block;
import application.mechanics.base.geometry.Coordinates;
import application.mechanics.base.Map;
import application.mechanics.utils.Index;
import application.mechanics.utils.MapHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@SuppressWarnings("InstanceVariableNamingConvention")
@Service
public class BlockService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockService.class.getSimpleName());

    private final int m;
    private final int n;
    private final Block[][] blocks;

    public BlockService() {
        m = Map.M;
        n = Map.N;
        blocks = new Block[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Map.MAP[i][j] > 0) {
                    final Coordinates corner = new Coordinates(
                            i * Config.BLOCK_SIZE - MapHelper.X_OFFSET,
                            Config.BLOCK_HEIGHT,
                            j * Config.BLOCK_SIZE - MapHelper.Z_OFFSET
                    );
                    final Block block = new Block(corner, Config.BLOCK_SIZE, Config.BLOCK_HEIGHT, Config.BLOCK_SIZE);
                    blocks[i][j] = block;
                }
            }
        }
    }

    public boolean isWallsBetween(@NotNull Coordinates from, @NotNull Coordinates to) {
        final Index fromIndex = MapHelper.getIndex(from);
        final Index toIndex = MapHelper.getIndex(to);
        LOGGER.info("from: {}, index: {}. to: {}, index: {}", from.toString(), "i " + fromIndex.i + ", j " + fromIndex.j,
                to.toString(), "i " + toIndex.i + ", j " + toIndex.j);
        final int fromI = Math.min(fromIndex.i, toIndex.i);
        final int toI = Math.max(fromIndex.i, toIndex.i);
        final int fromJ = Math.min(fromIndex.j, toIndex.j);
        final int toJ = Math.max(fromIndex.j, toIndex.j);
        for (int i = fromI; i <= toI; i++) {
            for (int j = fromJ; j <= toJ; j++) {
                LOGGER.info("block [{}][{}]", i, j);
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
}
