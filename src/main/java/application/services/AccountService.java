package application.services;

import application.db.Database;
import application.models.User;
import application.models.UserInfo;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class AccountService{
    private Database db;

    public AccountService(@NotNull Database db) {
        this.db = db;
    }

    private User getUser(@NotNull Long id) {
        return db.getUser(id);
    }

    public UserInfo getUserInfo(@NotNull Long id) {
        return getUser(id);
    }

    public boolean isUserExists(@NotNull Long id) {
        return db.hasUser(id);
    }

    public boolean isUserExists(@NotNull String username) {
        final Long id = getUserID(username);
        return id != null && isUserExists(id);
    }

    public boolean checkUserAccount(@NotNull Long id, @NotNull String password) {
        final User user = getUser(id);
        return doCheckPassword(user, password);
    }

    public Long signup(@NotNull User user) {
        return db.add(user);
    }

    public boolean changePassword(@NotNull Long id, @NotNull String oldPassword, @NotNull String newPassword) {
        final User user = getUser(id);
        if (!doCheckPassword(user, oldPassword)) {
            return false;
        }
        db.editUserPassword(id, newPassword);
        return true;
    }

    public Long getUserID(@NotNull String username) {
        return db.getUserID(username);
    }

    private boolean doCheckPassword(User user, @NotNull String password) {
        return user != null && user.getPassword().equals(password);
    }
}
