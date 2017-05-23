package application.mechanics;

import application.mechanics.base.UserSnap;
import application.mechanics.internal.ClientSnapService;
import application.mechanics.internal.ServerSnapService;
import application.mechanics.utils.TimeHelper;
import application.services.AccountService;
import application.websocket.RemotePointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Service
public class MechanicsExecutor {
    // TODO fix autowired
    @Autowired
    @NotNull
    private AccountService accountService;
    @Autowired
    @NotNull
    private ClientSnapService clientSnapshotsService;
    @Autowired
    @NotNull
    private ServerSnapService serverSnapshotService;
    @Autowired
    @NotNull
    private RemotePointService remotePointService;
    @Autowired
    @NotNull
    private ObjectMapper objectMapper;

    private final ThreadFactory threadFactory = new ThreadFactory(){
        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r);
            thread.setName("Game-mechanic");
            return thread;
        };
    };

    private final ExecutorService tickExecutors = Executors.newFixedThreadPool(Config.THREADS_NUM, threadFactory);

    private final GameMechanics[] gameMechanics = new GameMechanics[Config.THREADS_NUM];

    @PostConstruct
    public void initAfterStartup() {
        for (int i = 0; i < gameMechanics.length; ++i) {
            gameMechanics[i] = new GameMechanics(accountService,
                    serverSnapshotService, remotePointService, clientSnapshotsService, objectMapper);
            final Runnable worker = new Worker(gameMechanics[i]);
            tickExecutors.execute(worker);
        }
    }

    public void addUser (long user) {
        for (GameMechanics gameMechanic: gameMechanics) {
            final boolean hasSlots = gameMechanic.hasFreeSlots();
            if (hasSlots) {
                gameMechanic.addUser(user);
                return;
            }
        }

        int hasLessSessions = 0;
        int currentMin = gameMechanics[0].getSessionsNum();
        for (int i = 1; i < gameMechanics.length; ++i) {
            if (gameMechanics[i].getSessionsNum() < currentMin) {
                currentMin = gameMechanics[i].getSessionsNum();
                hasLessSessions = i;
            }
        }
        gameMechanics[hasLessSessions].addUser(user);
    }

    public void addClientSnapshot(long forUser, UserSnap message) {
        for (GameMechanics gameMechanic: gameMechanics) {
            if (gameMechanic.isPlaying(forUser)) {
                gameMechanic.addClientSnapshot(forUser, message);
                return;
            }
        }
    }


    public void removeUser(long user) {
        int i = 0;
        while (i < gameMechanics.length && !gameMechanics[i].removeUser(user)) {
            i++;
        }
    }


    private static class Worker implements Runnable {

        private final GameMechanics gameMechanics;

        Worker(GameMechanics gameMechanics) {
            this.gameMechanics = gameMechanics;
        }

        private final Clock clock = Clock.systemDefaultZone();

        @Override
        public void run() {
            long lastFrameMillis = Config.STEP_TIME;
            while (true) {
                final long before = clock.millis();

                gameMechanics.gmStep(lastFrameMillis);

                final long after = clock.millis();
                TimeHelper.sleep(Config.STEP_TIME - (after - before));

                if (Thread.currentThread().isInterrupted()) {
                    gameMechanics.reset();
                    return;
                }
                final long afterSleep = clock.millis();
                lastFrameMillis = afterSleep - before;
            }
        }
    }
}
