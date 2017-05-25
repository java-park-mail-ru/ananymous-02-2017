package application.mechanics.handlers;

import application.mechanics.MechanicsExecutor;
import application.mechanics.requests.Disconnect;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
//public class DisconnectUserHandler extends MessageHandler<Disconnect.Request> {
//    @NotNull
//    private final MechanicsExecutor mechanicExecutor;
//
//    public DisconnectUserHandler(@NotNull MechanicsExecutor mechanicsExecutor,
//                                 @NotNull MessageHandlerContainer messageHandlerContainer) {
//        super(Disconnect.Request.class);
//        this.mechanicExecutor = mechanicsExecutor;
//        messageHandlerContainer.registerHandler(clazz, this);
//    }
//
//    @Override
//    public void handle(@NotNull Disconnect.Request message, @NotNull Long forUser) throws HandleException {
//        mechanicExecutor.removeUser(forUser);
//    }
//}

@Component
public class DisconnectUserHandler extends MessageHandler<Disconnect.Request> {
    private final MechanicsExecutor mechanicExecutor;
    private final MessageHandlerContainer messageHandlerContainer;

    public DisconnectUserHandler(MechanicsExecutor mechanicExecutor, MessageHandlerContainer messageHandlerContainer) {
        super(Disconnect.Request.class);
        this.mechanicExecutor = mechanicExecutor;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(Disconnect.Request.class, this);
    }

    @Override
    public void handle(Disconnect.Request message, Long forUser) throws HandleException {
        mechanicExecutor.removeUser(forUser);
    }
}


