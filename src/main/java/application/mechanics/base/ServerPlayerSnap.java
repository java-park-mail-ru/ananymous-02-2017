package application.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ServerPlayerSnap {
    @NotNull
    private final Set<VictimModel> victims = new HashSet<>();

    @JsonProperty("id")
    @NotNull
    private Long userId;
    @NotNull
    private String login;
    @NotNull
    private Coordinates position;
    private int hp;
    private int kills;
    private int deaths;

    @NotNull
    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(@NotNull Coordinates position) {
        this.position = position;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @NotNull
    public Set<VictimModel> getVictims() {
        return victims;
    }

    public void setVictims(@NotNull Collection<VictimModel> victims) {
        this.victims.addAll(victims);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }
}
