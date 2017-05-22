package application.websocket;

import org.jetbrains.annotations.NotNull;

public class Message {
    @NotNull
    private String type;
    @NotNull
    private String data;

    // TODO remove this if possible
    @NotNull
    public static final String INITIALIZE_USER = "InitializePlayer";
    @NotNull
    public static final String SNAPSHOT = "Snapshot";
    @NotNull
    public static final String REMOVE_USER = "RemovePlayer";

    @NotNull
    public String getType() {
        return type;
    }
    @NotNull
    public String getData() {
        return data;
    }

    // TODO check for necessity
//    public Message() {
//    }

    public Message(@NotNull String type, @NotNull String data) {
        this.type = type;
        this.data = data;
    }

    public Message(@NotNull Class clazz, @NotNull String data) {
        this(clazz.getName(), data);
    }
}
