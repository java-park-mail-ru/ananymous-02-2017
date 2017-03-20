package application.utils;

import application.utils.requests.UserRequest;
import application.utils.requests.UsernameRequest;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isEmail(@NotNull String email) {
        return doValidate(email, "^([a-zA-Z0-9_\\.-]+)@([\\da-zA-Z\\.-]+)\\.([a-z\\.]{2,6})$");
    }
    public static boolean isLogin(@NotNull String login) {
        return doValidate(login, "^[a-zA-Z0-9_-]{3,20}$");
    }
    public static boolean isPassword(@NotNull String password) {
        return password != null && password.length() >= 8;
    }
    public static @Nullable String getUserError(@NotNull UserRequest user) {
        if (!isLogin(user.getLogin())) {
            return "Invalid login: " + user.getLogin();
        } else if (!isEmail(user.getEmail())) {
            return "Invalid email: " + user.getEmail();
        } else if (!isPassword(user.getPassword())) {
            return "Invalid password";
        }
        return null;
    }
    public static @Nullable String getUserRequestError(@NotNull UsernameRequest user) {
        if (user.getUsername() == null) {
            return "Invalid username: " + user.getUsername();
        } else if (!isPassword(user.getPassword())) {
            return "Invalid password";
        }
        return null;
    }

    private static boolean doValidate(String str, String pattern) {
        if (str == null) {
            return false;
        }
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(str);
        return m.matches();
    }
}
