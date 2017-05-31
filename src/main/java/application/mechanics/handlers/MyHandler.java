package application.mechanics.handlers;

import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyHandler extends MessageHandler<String> {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(MyHandler.class.getSimpleName());

    @Override
    public void handle(@NotNull String message, long forUser) throws HandleException {
        LOGGER.info("MyHandler. " + message);
    }

    public MyHandler(@NotNull MessageHandlerContainer messageHandlerContainer) {
        super(String.class);
        messageHandlerContainer.registerHandler(clazz, this);
    }
}

