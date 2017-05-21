package application.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public abstract class MessageHandler<T> {
    @NotNull
    protected final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(@NotNull Message message, @NotNull Long forUser) throws HandleException {
        try {
            final Object data = new ObjectMapper().readValue(message.getData(), clazz);
            handle(clazz.cast(data), forUser);
        } catch (IOException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type " + message.getType() + " with content: " +
                    message.getData(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Long forUser) throws HandleException;
}
