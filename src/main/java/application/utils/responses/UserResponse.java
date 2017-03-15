package application.utils.responses;

public class UserResponse {
    private final String login;
    private final String email;

    public UserResponse(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public String getLogin() {

        return login;
    }

    public String getEmail() {
        return email;
    }
}
