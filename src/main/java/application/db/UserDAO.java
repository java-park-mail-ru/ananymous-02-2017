package application.db;

import application.models.User;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface UserDAO {
    @NotNull Long add(@NotNull String login, @NotNull String email, @NotNull String password);

    @Nullable User getUser(@NotNull Long id);

    boolean hasUser(@NotNull Long id);

    void editUserPassword(@NotNull User user, @NotNull String password);

    @Nullable Long getUserID(@NotNull String username);

    Collection<User> getAllUsers();
}
