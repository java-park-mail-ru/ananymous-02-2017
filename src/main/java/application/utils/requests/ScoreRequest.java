package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class ScoreRequest {
    @NotNull
    Long id;
    int sScore;
    int mScore;

    @JsonCreator
    public ScoreRequest(@JsonProperty("id") @NotNull Long id,
                        @JsonProperty(value = "sScore", defaultValue = "0") int sScore,
                        @JsonProperty(value = "mScore", defaultValue = "0") int mScore) {
        this.id = id;
        this.sScore = sScore;
        this.mScore = mScore;
    }

    @NotNull
    public Long getId() {
        return id;
    }

    public int getsScore() {
        return sScore;
    }

    public int getmScore() {
        return mScore;
    }
}
