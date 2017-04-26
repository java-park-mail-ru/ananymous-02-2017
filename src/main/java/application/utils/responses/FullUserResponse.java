package application.utils.responses;

public class FullUserResponse {
    private final long id;
    private final String login;
    private final String email;

    public FullUserResponse(long id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
