package application.mechanics.base;

import org.jetbrains.annotations.NotNull;

public class VictimModel {
    @NotNull
    private Long id;
    @NotNull
    private String login;

    // TODO check
    public VictimModel() {

    }

    public VictimModel(@NotNull Long id, @NotNull String login) {
        this.id = id;
        this.login = login;
    }

    @NotNull
    public Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }
}
