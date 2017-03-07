package application.db;

import application.models.User;

import javax.validation.constraints.NotNull;

public interface Database {
    Long add(@NotNull User user);

    User getUser(@NotNull Long id);

    boolean hasUser(@NotNull Long id);

    void editUserPassword(@NotNull Long id, @NotNull String password);

    Long getUserID(@NotNull String username);
}
