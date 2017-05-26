package application.mechanics.internal;

import application.mechanics.GameSession;
import application.mechanics.avatar.GameUser;
import application.mechanics.base.ServerPlayerSnap;
import application.mechanics.base.ServerSnap;
import application.websocket.Message;
import application.websocket.RemotePointService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ServerSnapService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapService.class.getSimpleName());

    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerSnapService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        final List<ServerPlayerSnap> playersSnaps = new ArrayList<>();
        for (GameUser player : players) {
            final ServerPlayerSnap serverPlayerSnap = player.generateSnap();
            playersSnaps.add(serverPlayerSnap);
        }

        if (playersSnaps.isEmpty()) {
            throw new RuntimeException("No players snaps");
        }

        final ServerSnap snap = new ServerSnap();
        snap.setPlayers(playersSnaps);
        try {
            final Message message = new Message();
            message.setType(Message.SNAPSHOT);
            for (GameUser player : players) {
                // TODO normal setter
                snap.setShot(player.getShot());
                snap.setHp(player.getHp());
                snap.setKills(player.getKills());
                snap.setDeaths(player.getDeaths());

                message.setData(objectMapper.writeValueAsString(snap));
                remotePointService.sendMessageToUser(player.getId(), message);
                player.resetForNextSnap();
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing JSON {}", snap.toString());
        } catch (IOException e) {
            LOGGER.error("Error sending server snap! {}", e.getMessage());
        }

    }
}

