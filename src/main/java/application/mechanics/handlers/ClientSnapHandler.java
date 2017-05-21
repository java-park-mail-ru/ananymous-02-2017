//package application.mechanics.handlers;
//
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//public class ClientSnapHandler extends MessageHandler<UserSnap> {
//
//    public ClientSnapHandler(MechanicsExecutor mechanicsExecutor, MessageHandlerContainer messageHandlerContainer) {
//        super(UserSnap.class);
//        this.mechanicsExecutor = mechanicsExecutor;
//        this.messageHandlerContainer = messageHandlerContainer;
//    }
//
//    @PostConstruct
//    private void init() {
//        messageHandlerContainer.registerHandler(UserSnap.class, this);
//    }
//
//    @Override
//    public void handle(UserSnap message, long forUser) throws HandleException {
//        mechanicsExecutor.addClientSnapshot(forUser, message);
//    }
//}
