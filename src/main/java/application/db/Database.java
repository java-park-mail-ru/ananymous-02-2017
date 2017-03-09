package application.db;

import application.models.User;
import application.models.UserInfo;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public interface Database {
    Long add(@NotNull User user);

    User getUser(@NotNull Long id);

    boolean hasUser(@NotNull Long id);

    void editUserPassword(@NotNull Long id, @NotNull String password);

    Long getUserID(@NotNull String username);

//    Collection<User> getAllUsers();
}
