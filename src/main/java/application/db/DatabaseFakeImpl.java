package application.db;

import application.models.User;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DatabaseFakeImpl implements Database {
    private Map<Long, User> db;
    private Map<String, Long> idMap;
    private static final AtomicLong ID_GEN = new AtomicLong(0);

    public DatabaseFakeImpl() {
        db = new HashMap<>();
        idMap = new HashMap<>();
    }

    @Override
    public Long add(@NotNull User user) {
        Long id = ID_GEN.getAndIncrement();
        idMap.put(user.getLogin(), id);
        idMap.put(user.getEmail(), id);
        db.put(id, user);
        return id;
    }

    @Override
    public User getUser(@NotNull Long id) {
        return db.get(id);
    }

    @Override
    public boolean hasUser(@NotNull Long id) {
        return db.containsKey(id);
    }

    @Override
    public void editUserPassword(@NotNull Long id, @NotNull String password) {
        getUser(id).setPassword(password);
    }

    @Override
    public Long getUserID(@NotNull String username) {
        return idMap.get(username);
    }
}
