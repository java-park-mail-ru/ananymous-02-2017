package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ScoreRequest {
    @NotNull
    String login;
    int sScore;
    int mScore;

    @JsonCreator
    public ScoreRequest(@JsonProperty("login") @NotNull String login,
                        @JsonProperty(value = "sScore", defaultValue = "0") int sScore,
                        @JsonProperty(value = "mScore", defaultValue = "0") int mScore) {
        this.login = login;
        this.sScore = sScore;
        this.mScore = mScore;
    }

    public String getLogin() {
        return login;
    }

    public int getsScore() {
        return sScore;
    }

    public int getmScore() {
        return mScore;
    }
}
