package application.models;


import javax.validation.constraints.NotNull;

public class UserProfile {
    private long id;
    @NotNull
    private User user;

    public UserProfile() {}

    public UserProfile(long id, User user) {
        this.id = id;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
