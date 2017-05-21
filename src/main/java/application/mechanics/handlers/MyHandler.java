package application.mechanics.handlers;

import application.mechanics.requests.Disconnect;
import application.websocket.HandleException;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MyHandler extends MessageHandler<String> {
    private final MessageHandlerContainer messageHandlerContainer;

    @Override
    public void handle(@NotNull String message, @NotNull Long forUser) throws HandleException {
        System.out.println(message);
    }

    public MyHandler(@NotNull MessageHandlerContainer messageHandlerContainer) {
        super(String.class);
        this.messageHandlerContainer = messageHandlerContainer;
        messageHandlerContainer.registerHandler(clazz, this);
    }

//    public DisconnectUserHandler(MechanicsExecutor mechanicExecutor, MessageHandlerContainer messageHandlerContainer) {
//        super(Disconnect.Request.class);
//        this.mechanicExecutor = mechanicExecutor;
//        this.messageHandlerContainer = messageHandlerContainer;
//    }

//    @PostConstruct
//    private void init() {
//        messageHandlerContainer.registerHandler(Disconnect.Request.class, this);
//    }



//    @Override
//    public void handle(Disconnect.Request message, long forUser) throws HandleException {
//        mechanicExecutor.removeUser(forUser);
//    }
}

