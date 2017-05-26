package application.mechanics.internal;

import application.mechanics.GameSession;
import application.mechanics.avatar.GameUser;
import application.mechanics.base.*;
import application.services.AccountService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientSnapService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnapService.class);

    @NotNull
    private final Map<Long, List<UserSnap>> userToSnaps = new HashMap<>();

    private double damageCoeff;
    // TODO fix autowired
    @Autowired
    @NotNull
    private AccountService accountService;
    @NotNull
    @Autowired
    private BlockService blockService;

    private static final int RADIUS = 3;
    public static final int SCORES_FOR_SHOT = 2;
    public static final int SCORES_FOR_KILL = 10;
    public static final double DAMAGE_COEFF_MIN = 0.5;


    public synchronized void pushClientSnap(@NotNull Long user, @NotNull UserSnap snap) {
        final List<UserSnap> userSnaps = userToSnaps.computeIfAbsent(user, u -> new ArrayList<>());
        userSnaps.add(snap);
    }

    @Nullable
    public synchronized List<UserSnap> getSnapsForUser(@NotNull Long user) {
        return userToSnaps.get(user);
    }

    public void processSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        for (GameUser player : players) {
            final List<UserSnap> playerSnaps = getSnapsForUser(player.getId());
            if (playerSnaps == null || playerSnaps.isEmpty()) {
                continue;
            }
            final UserSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
            player.setPosition(lastSnap.getPosition());

            for (UserSnap snap: playerSnaps) {
                if (!snap.isFiring()) {
                    continue;
                }
                final GameUser victim = processFiring(snap, players);
                if (victim != null) {
                    accountService.addScore(player.getId(), 0, SCORES_FOR_SHOT);
                    victim.markShot(damageCoeff);
                    if (!victim.isAlive()) {
                        final VictimModel model =
                                new VictimModel(victim.getId(), victim.getUser().getLogin());
                        player.addVictim(model);
                        accountService.addScore(player.getId(), 0, SCORES_FOR_KILL);
                    }
                }
            }
        }
    }

    @Nullable
    private GameUser processFiring(UserSnap snap, Iterable<GameUser> players) {
        final Coordinates myPosition = snap.getPosition();
        LOGGER.info("FIRING:myPosition. " + myPosition.toString());

        final Coordinates cameraDirection  = snap.getCamera();
        LOGGER.info("FIRING:cameraDirection. " + cameraDirection.toString());
        cameraDirection.y *= -1;
        
        final MyVector currentShot = new MyVector(cameraDirection);

        for (GameUser player: players) {
            if (player.getId() == snap.getId()) {
                continue;
            }
            final Coordinates enemyPosition = player.getPosition();
            if (enemyPosition == null) {
                continue;
            }

            final MyVector idealShot = new MyVector(enemyPosition.subtract(myPosition));

            final double distance = enemyPosition.getDistanceBetween(myPosition);
            final double hypotenuse = Math.hypot(distance, RADIUS);

            final double maxCos = distance / hypotenuse;

            final double cos = currentShot.getCos(idealShot);
            if (cos >= maxCos) {
                if (!noWallsBetween(myPosition, enemyPosition, currentShot)) {
                    continue;
                }
                LOGGER.info("Shot in target");
                final double shotLenght = distance / cos;
                final double distanceFromEnemyCenter =
                        Math.sqrt(shotLenght * shotLenght - distance * distance);
                
                damageCoeff = (RADIUS - distanceFromEnemyCenter) / RADIUS;
                if (damageCoeff < DAMAGE_COEFF_MIN) {
                    damageCoeff = DAMAGE_COEFF_MIN;
                }
                return player;
            }
        }
        return null;
    }

    private boolean noWallsBetween(@NotNull Coordinates killer, @NotNull Coordinates enemy, @NotNull MyVector camera) {
        final Set<Block> blocks = blockService.getBlocks();
        for (Block block: blocks) {
            final Ray shotRay = new Ray(camera, killer);
            final Double distanceToBlock = block.isOnTheWay(shotRay);
            if (distanceToBlock != null) {
                final double distanceToEnemy = killer.getDistanceBetween(enemy);
                if (distanceToBlock < distanceToEnemy) {
                    LOGGER.info("Shot in wall");
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        userToSnaps.clear();
    }
}

