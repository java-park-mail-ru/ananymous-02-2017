package application.utils.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdResponse {
    private final long id;

    public IdResponse(@JsonProperty("id") long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
