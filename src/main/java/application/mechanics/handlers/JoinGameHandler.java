package application.mechanics.handlers;

import application.mechanics.MechanicsExecutor;
import application.mechanics.requests.JoinGame;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
//public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
//    @NotNull
//    private final MechanicsExecutor mechanicsExecutor;
//
//    public JoinGameHandler(@NotNull MechanicsExecutor mechanicsExecutor,
//                           @NotNull MessageHandlerContainer messageHandlerContainer) {
//        super(JoinGame.Request.class);
//        this.mechanicsExecutor = mechanicsExecutor;
//        messageHandlerContainer.registerHandler(clazz, this);
//    }
//
//    @Override
//    public void handle(@NotNull JoinGame.Request message, @NotNull Long forUser) throws HandleException {
//        mechanicsExecutor.addUser(forUser);
//    }
//}

@Component
public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
    private final MechanicsExecutor mechanicsExecutor;
    private final MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(MechanicsExecutor mechanicsExecutor, MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.mechanicsExecutor = mechanicsExecutor;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.Request.class, this);
    }

    @Override
    public void handle(JoinGame.Request message, Long forUser) throws HandleException {
        mechanicsExecutor.addUser(forUser);
    }
}