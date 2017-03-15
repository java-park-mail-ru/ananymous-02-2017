package application.db;

import application.models.User;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class UserDAOFakeImpl implements UserDAO {
    private final Map<Long, User> db = new HashMap<Long, User>();
    private final Map<String, Long> idMap = new HashMap<String, Long>();
    private static final AtomicLong ID_GEN = new AtomicLong(0);

    @Override
    @NotNull
    public Long add(@NotNull String login, @NotNull String email, @NotNull String password) {
        final Long id = ID_GEN.getAndIncrement();
        idMap.put(login, id);
        idMap.put(email, id);
        final User user = new User(id, login, email, password);
        db.put(id, user);
        return id;
    }

    @Override
    public @Nullable User getUser(@NotNull Long id) {
        return db.get(id);
    }

    @Override
    public boolean hasUser(@NotNull Long id) {
        return db.containsKey(id);
    }

    @Override
    public void editUserPassword(@NotNull User user, @NotNull String password) {
        db.put(user.getId(), new User(user.getId(), user.getLogin(), user.getEmail(), password));
    }

    @Override
    public @Nullable Long getUserID(@NotNull String username) {
        return idMap.get(username);
    }

    @Override
    public Collection<User> getAllUsers() {
        return db.values();
    }
}
