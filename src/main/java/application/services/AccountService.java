package application.services;

import application.db.Database;
import application.models.User;
import application.models.UserInfo;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Iterator;

@Service
public class AccountService{
    private Database db;

    public AccountService(@NotNull Database db) {
        this.db = db;
    }

    private User getUser(@NotNull Long id) {
        return db.getUser(id);
    }

    public @Nullable UserInfo getUserInfo(@NotNull Long id) {
        final User user = getUser(id);
        return user == null ? null : user.getUserInfo();
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

    public @Nullable Long getUserID(@NotNull String username) {
        return db.getUserID(username);
    }

    private boolean doCheckPassword(@Nullable User user, @NotNull String password) {
        return user != null && user.getPassword().equals(password);
    }

    public UserInfo[] getAllUsers() {
        final Collection<User> collection = db.getAllUsers();
        UserInfo[] arr = new UserInfo[collection.size()];
        Iterator<User> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            arr[i++] = it.next().getUserInfo();
        }
        return arr;
    }
}
