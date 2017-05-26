package application.websocket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameMessageHandlerContainer implements MessageHandlerContainer {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMessageHandlerContainer.class.getSimpleName());

    final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    @Override
    public void handle(@NotNull Message message, long forUser) throws HandleException {
        LOGGER.info("handle in container for user " + forUser);
        final Class clazz;
        try {
            LOGGER.info("start getting class for name");
            clazz = Class.forName(message.getType());
            LOGGER.info("end getting class for name");
        } catch (ClassNotFoundException e) {
            throw new HandleException("Can't handle message of " + message.getType() + " type", e);
        }
        LOGGER.info("get message handler");
        final MessageHandler<?> messageHandler = handlerMap.get(clazz);
        if (messageHandler == null) {
            throw new HandleException("No handler for message of " + message.getType() + " type");
        }
        LOGGER.info("handle message by handler for user " + forUser);
        messageHandler.handleMessage(message, forUser);
        LOGGER.debug("message handled: type =[" + message.getType() + "], content=[" + message.getData() + ']');
    }

    @Override
    public <T> void registerHandler(@NotNull Class<T> clazz, @NotNull MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }
}
