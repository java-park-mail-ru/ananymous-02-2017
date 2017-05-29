package application.mechanics.internal;

import application.mechanics.Config;
import application.mechanics.base.Block;
import application.mechanics.base.geometry.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BlockService {
    @NotNull
    private final Set<Block> blocks = new HashSet<>();

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
        final int halfSize = Config.BLOCK_SIZE;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    final Coordinates center = new Coordinates(
                            halfSize + i * Config.BLOCK_SIZE,
                            Config.BLOCK_HEIGHT / 2,
                            halfSize + j * Config.BLOCK_SIZE
                    );
                    final Block block = new Block(center, Config.BLOCK_SIZE, Config.BLOCK_HEIGHT, Config.BLOCK_SIZE);
                    blocks.add(block);
                }
            }
        }
    }

    @NotNull
    public Set<Block> getBlocks() {
        return blocks;
    }
}
