package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserDAO {
    @NotNull
    Long add(@NotNull String login, @NotNull String email, @NotNull String password);

    @Nullable
    User getUser(@NotNull Long id);

    boolean hasUser(@NotNull Long id);

    void editUserPassword(@NotNull User user, @NotNull String password);

    @Nullable
    Long getUserID(@NotNull String username);

    @NotNull
    List<User> getUsers(int beg, int size);

    @NotNull
    List<User> getUsers();
}
