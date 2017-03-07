package application.services;

import application.models.User;
import application.models.UserProfile;
import application.exceptions.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AccountService{

    private Map<String, UserProfile> DB = new HashMap<>();
    private final AtomicLong ID_GEN = new AtomicLong(0);

    public String signup(@NotNull User user)
            throws RequestException
    {
        if (DB.containsKey(user.getLogin())) {
            throw new RequestException(HttpStatus.CONFLICT, "This user already exist");
        }
        UserProfile userProfile = new UserProfile(ID_GEN.getAndIncrement(), user);
        DB.put(user.getLogin(), userProfile);
        return user.getLogin();
    }

    public String login(@NotNull String login, @NotNull String password)
            throws RequestException
    {
        if (!DB.containsKey(login)) {
            throw new RequestException(HttpStatus.NOT_FOUND, "User not found");
        }
        UserProfile userProfile = DB.get(login);
        checkIfLoginIsCorrect(userProfile.getUser(), password);
        return login;
    }

    public User getUser(@NotNull String userID)
            throws RequestException
    {
        if (!DB.containsKey(userID)) {
            throw new RequestException(HttpStatus.NOT_FOUND, "User not found");
        }
        return DB.get(userID).getUser();
    }

    public void changePassword(@NotNull User user, @NotNull String oldPassword, @NotNull String newPassword)
            throws RequestException
    {
        checkIfLoginIsCorrect(user, oldPassword);
        user.setPassword(newPassword);
        DB.get(user.getLogin()).setUser(user);
    }

    private void checkIfLoginIsCorrect(@NotNull User user, @NotNull String password)
            throws RequestException
    {
        if (!user.getPassword().equals(password)) {
            throw new RequestException(HttpStatus.FORBIDDEN, "Wrong login or password");
        }
    }


}
