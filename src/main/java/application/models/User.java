package application.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User extends UserInfo {
    private String password;

    public User() {}

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        super(login, email);
        this.password = password;
    }

    public UserInfo getUserInfo() {
        return new UserInfo(login, email);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
