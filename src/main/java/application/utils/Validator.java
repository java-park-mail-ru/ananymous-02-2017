package application.utils;

import application.utils.requests.UserRequest;
import application.utils.requests.UsernameRequest;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final int LOGIN_MAX_SIZE = 50;
    private static final int EMAIL_MAX_SIZE = 50;
    private static final int PASSWORD_MAX_SIZE = 200;

    public static @Nullable String checkEmail(@NotNull String email) {
        if (email.length() > EMAIL_MAX_SIZE) {
            return "Max lenght of email is " + EMAIL_MAX_SIZE;
        } else if (!doValidate(email, "^([a-zA-Z0-9_\\.-]+)@([\\da-zA-Z\\.-]+)\\.([a-z\\.]{2,6})$")) {
            return "Wrong email scheme";
        }

        return null;
    }

    public static @Nullable String checkLogin(@NotNull String login) {
        if (login.length() > LOGIN_MAX_SIZE) {
            return "Max lenght of login is " + LOGIN_MAX_SIZE;
        } else if (!doValidate(login, "^[a-zA-Z0-9_-]{3,}$")) {
            return "Login must contain only letters, digits, '_' and '-' and contain at least 3 symbols";
        }

        return null;
    }

    public static @Nullable String checkPassword(@NotNull String password) {
        if (password.length() > PASSWORD_MAX_SIZE) {
            return "Max lenght of password is " + PASSWORD_MAX_SIZE;
        } else if (password.length() < 8) {
            return "Password must be at least 8 symbols";
        }

        return null;
    }

    public static @Nullable String getUserError(@NotNull UserRequest user) {
        final String login = user.getLogin();
        final String email = user.getEmail();
        final String password = user.getPassword();
        if (StringUtils.isEmpty(login)) {
            return "Login is empty";
        } else if (StringUtils.isEmpty(email)) {
            return "Email is empty";
        } else if (StringUtils.isEmpty(password)) {
            return "Password is empty";
        }

        String error;
        if ((error = checkLogin(login)) != null) {
            return error;
        }

        if ((error = checkEmail(email)) != null) {
            return error;
        }

        if ((error = checkPassword(password)) != null) {
            return error;
        }

        return null;
    }

    public static @Nullable String getUserRequestError(@NotNull UsernameRequest user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        if (StringUtils.isEmpty(username)) {
            return "Username is empty";
        } else if (StringUtils.isEmpty(password)) {
            return "Password is empty";
        }

        final String error;
        if ((error = checkPassword(password)) != null) {
            return error;
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
