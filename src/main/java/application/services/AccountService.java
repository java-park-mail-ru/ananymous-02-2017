package application.services;

import application.db.UserDAO;
import application.models.User;
import application.utils.requests.UserRequest;
import application.utils.responses.FullUserResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Iterator;

@Service
public class AccountService{
    private final UserDAO db;
    private final PasswordEncoder encoder;

    public AccountService(@NotNull UserDAO db, @NotNull PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    public @Nullable User getUser(@NotNull Long id) {
        return db.getUser(id);
    }

    public boolean isUserExists(@NotNull Long id) {
        return db.hasUser(id);
    }

    private boolean isUserExists(@NotNull String username) {
        final Long id = getUserID(username);
        return id != null && isUserExists(id);
    }

    public boolean checkUserAccount(@NotNull Long id, @NotNull String password) {
        final User user = getUser(id);
        return user != null && doCheckPassword(user, password);
    }

    public Long signup(@NotNull UserRequest user) {
        final String encodedPassword = encoder.encode(user.getPassword());
        return db.add(user.getLogin(), user.getEmail(), encodedPassword);
    }

    public boolean changePassword(@NotNull User user, @NotNull String oldPassword, @NotNull String newPassword) {
        if (!doCheckPassword(user, oldPassword)) {
            return false;
        }
        final String encodedNewPassword = encoder.encode(newPassword);
        db.editUserPassword(user, encodedNewPassword);
        return true;
    }

    public @Nullable Long getUserID(@NotNull String username) {
        return db.getUserID(username);
    }

    private boolean doCheckPassword(@NotNull User user, @NotNull String password) {
        return encoder.matches(password, user.getPassword());
    }

    public FullUserResponse[] getAllUsers() {
        final Collection<User> collection = db.getAllUsers();
        final FullUserResponse[] arr = new FullUserResponse[collection.size()];
        final Iterator<User> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            final User user = it.next();
            arr[i++] = new FullUserResponse(user.getId(), user.getLogin(), user.getEmail());
        }
        return arr;
    }

    public boolean isUniqueLogin(String login) {
        return isUserExists(login);
    }

    public boolean isUniqueEmail(String email) {
        return isUserExists(email);
    }
}