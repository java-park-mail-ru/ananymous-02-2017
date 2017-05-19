package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO{
    private static final String TABLE_NAME = "users";
    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Autowired
    private JdbcTemplate template;

    @Override
    public @Nullable Long add(@NotNull String login, @NotNull String email, @NotNull String password, int sScore, int mScore) {
        try {
            final KeyHolder idHolder = new GeneratedKeyHolder();
            template.update(con -> {
                final String query = "INSERT INTO users (login, email, password, sscore, mscore) VALUES (?, ?, ?, ?, ?)";
                final PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, login);
                pst.setString(2, email);
                pst.setString(3, password);
                pst.setInt(4, sScore);
                pst.setInt(5, mScore);
                return pst;
            }, idHolder);
            final Map<String, Object> keys = idHolder.getKeys();
            if (keys.size() > 1) {
                // postgres
                return ((Integer) keys.get("id")).longValue();
            } else {
                // h2
                return idHolder.getKey().longValue();
            }
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    @Override
    public @Nullable User getUser(@NotNull Long id) {
        try {
            final String query = "SELECT * FROM users WHERE id = ?";
            return template.queryForObject(query, USER_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void editUser(@NotNull Long id, @Nullable String newLogin, @Nullable String newEmail, @Nullable String newPassword,
                         @Nullable Integer newSScore, @Nullable Integer newMScore) {
        String query = "UPDATE users SET " +
                "login = COALESCE (?, login), " +
                "email = COALESCE (?, email), " +
                "password = COALESCE (?, password), " +
                "sscore = COALESCE (?, sscore), " +
                "mscore = COALESCE (?, mscore) " +
                "WHERE id = ?";
        template.update(query, newLogin, newEmail, newPassword, newSScore, newMScore, id);
    }

    @Override
    public void addScore(@NotNull Long id, int sScore, int mScore) {
        String query = "UPDATE users SET " +
                "sscore = sscore + ?, " +
                "mscore = mscore + ?" +
                "WHERE id = ?";
        template.update(query, sScore, mScore, id);
    }

    @Override
    public @Nullable Long getUserID(@NotNull String username) {
        try {
            final String query = "SELECT id FROM users WHERE LOWER(login) = LOWER(?) OR LOWER(email) = LOWER(?)";
            return template.queryForObject(query, Long.class, username, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public @NotNull List<User> getBestUsers(int beg, int size) {
        final String query = "SELECT u.* FROM users u JOIN " +
                "(SELECT id FROM users u ORDER BY sScore DESC LIMIT ? OFFSET ?) j ON (j.id = u.id)" +
                " ORDER BY sScore DESC";
        return template.query(query, USER_ROW_MAPPER, size, beg);
    }

    @Override
    public @NotNull List<User> getBestUsers() {
        final String query = "SELECT * FROM users ORDER BY sScore DESC";
        return template.query(query, USER_ROW_MAPPER);
    }

    @Override
    public void clear() {
        template.execute("TRUNCATE TABLE users");
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet, rowNum) -> {
        Long id = resultSet.getLong("id");
        String login = resultSet.getString("login");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        int sscore = resultSet.getInt("sscore");
        int mscore = resultSet.getInt("mscore");
        return new User(id, login, email, password, sscore, mscore);
    };
}
