package application.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    protected String login;
    protected String email;

    public UserInfo() {}

    @JsonCreator
    public UserInfo(@JsonProperty("login") String login,
                    @JsonProperty("email") String email) {
        this.login = login;
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
