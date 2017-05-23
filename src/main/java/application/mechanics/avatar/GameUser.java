package application.mechanics.avatar;

import application.mechanics.Config;
import application.mechanics.base.Coordinates;
import application.mechanics.base.ServerPlayerSnap;
import application.mechanics.base.VictimModel;
import application.models.User;

import java.util.HashSet;
import java.util.Set;

public class GameUser {
    private final User user;

    private Coordinates position;
    private int hp = 100;
    private boolean wasShot;
    private final Set<VictimModel> victims = new HashSet<>();

    private int kills = 0;
    private int deaths = 0;

    public GameUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setPosition(Coordinates coords) {
        this.position = coords;
    }

    public Coordinates getPosition() {
        return position;
    }

    public long getId() {
        return user.getId();
    }

    public void resetForNextSnap() {
        wasShot = false;
        victims.clear();
        if (hp == 0) {
            setFullHealth();
        }
    }

    public ServerPlayerSnap generateSnap() {
        final ServerPlayerSnap result = new ServerPlayerSnap();
        result.setUserId(getId());
        result.setPosition(position);
        result.setHp(hp);
        result.setVictims(victims);
        result.setKills(kills);
        result.setDeaths(deaths);
        result.setLogin(user.getLogin());
        victims.clear();
        return result;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void markShot(double coefficient) {
        wasShot = true;
        hp -= Config.SHOT_REDUCING * coefficient;
        if (hp < 0) {
            hp = 0;
            deaths++;
        }
    }

    public void addVictim (VictimModel victim) {
        victims.add(victim);
        kills++;
    }

    public boolean getShot() {
        return wasShot;
    }

    public int getHp() {
        return hp;
    }

    private void setFullHealth() {
        hp = 100;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameUser other = (GameUser) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        final long id = user.getId();
        return (int) (id ^ (id >>> 32));
    }
}

