package application;

public class UserResponse {
    private String login;
    private String email;

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
