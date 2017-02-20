package sample;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Solovyev on 18/02/2017.
 */
@Service
public class AccountService {

    private Map<String, UserProfile> userNameToUserProfile = new HashMap<>();

    @NotNull
    public UserProfile register(@NotNull String name, @NotNull String login, @NotNull String password) {

        //your code here
        return null;
    }

    public boolean login(@NotNull String name, @NotNull String password) {
        //your code here

        return false;
    }

    //we need more methods
}
