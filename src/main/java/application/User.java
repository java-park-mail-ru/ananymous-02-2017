package application;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String login;
    private String email;
    private String password;

    public User() {}

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
