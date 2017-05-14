package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserDAO {
    @Nullable
    Long add(@NotNull String login, @NotNull String email, @NotNull String password, int sScore, int mScore);

    @Nullable
    User getUser(@NotNull Long id);

    void editUser(@NotNull Long id, @Nullable String login, @Nullable String email, @Nullable String password,
                  @Nullable Integer sScore, @Nullable Integer mScore);

    void addScore(@NotNull Long id, int sScore, int mScore);

    @Nullable
    Long getUserID(@NotNull String username);

    @NotNull
    List<User> getUsers(int beg, int size);

    @NotNull
    List<User> getUsers();

    void clear();
}
