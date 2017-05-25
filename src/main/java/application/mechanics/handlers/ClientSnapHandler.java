package application.mechanics.handlers;

import application.mechanics.MechanicsExecutor;
import application.mechanics.base.UserSnap;
import application.websocket.GameMessageHandlerContainer;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
//public class ClientSnapHandler extends MessageHandler<UserSnap> {
//    @NotNull
//    MechanicsExecutor mechanicsExecutor;
//
//    public ClientSnapHandler(@NotNull MechanicsExecutor mechanicsExecutor,
//                             @NotNull MessageHandlerContainer messageHandlerContainer) {
//        super(UserSnap.class);
//        this.mechanicsExecutor = mechanicsExecutor;
//        messageHandlerContainer.registerHandler(clazz, this);
//    }
//
//    @Override
//    public void handle(@NotNull UserSnap message, @NotNull Long forUser) throws HandleException {
//        mechanicsExecutor.addClientSnapshot(forUser, message);
//    }
//}

@Component
public class ClientSnapHandler extends MessageHandler<UserSnap> {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnapHandler.class);

    private final MechanicsExecutor mechanicsExecutor;
    private final MessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(MechanicsExecutor mechanicsExecutor, MessageHandlerContainer messageHandlerContainer) {
        super(UserSnap.class);
        this.mechanicsExecutor = mechanicsExecutor;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(UserSnap.class, this);
    }

    @Override
    public void handle(UserSnap message, Long forUser) throws HandleException {
        LOGGER.info("ClientSnapHandler, handle");
        LOGGER.info("mechanicsExecutor != null : " + (mechanicsExecutor != null));
        mechanicsExecutor.addClientSnapshot(forUser, message);
        LOGGER.info("ClientSnapHandler, end handling");
    }
}

