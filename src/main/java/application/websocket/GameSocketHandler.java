package application.websocket;

import application.controllers.BaseController;
import application.mechanics.requests.Disconnect;
import application.mechanics.requests.JoinGame;
import application.models.User;
import application.services.AccountService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class GameSocketHandler extends TextWebSocketHandler {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class.getName());

    private static final String USER_ID = BaseController.USER_ID;

    @NotNull
    private final AccountService accountService;
    @NotNull
    private final MessageHandlerContainer messageHandlerContainer;
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameSocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                             @NotNull AccountService accountService,
                             @NotNull RemotePointService remotePointService) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.accountService = accountService;
        this.remotePointService = remotePointService;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession webSocketSession) throws AuthenticationException {
        LOGGER.info("ConnectionEstablished");
        final Long id = (Long) webSocketSession.getAttributes().get(USER_ID);
        final User user;
        if (id == null || (user = accountService.getUser(id)) == null) {
            LOGGER.error("Only authenticated users allowed to play a game!");
            return;
        }

        if (remotePointService.get(id) != null) {
            LOGGER.error("You are already playing");
            return;
        }
        LOGGER.info("New user {} #{}", user.getLogin(), user.getId());
        remotePointService.registerUser(user.getId(), webSocketSession);


        LOGGER.info("afterConnectionEstablished, session: " + webSocketSession.toString() + ", id: " + user.getId());
        sendIdToClient(webSocketSession, user.getId());

        LOGGER.info("Send JoinGame.Request");

        final Message message = new Message(JoinGame.Request.class, "{}");
        try {
            messageHandlerContainer.handle(message, user.getId());
        } catch (HandleException e) {
            LOGGER.error("Can't handle message while handshaking");
        }
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session,
                                     @NotNull TextMessage textMessage) throws AuthenticationException {
        LOGGER.info("handleTextMessage");
        final Long userId = (Long) session.getAttributes().get(USER_ID);

        LOGGER.info("handleTextMessage, session: " + session.toString() + ", id: " + userId);
        sendIdToClient(session, userId);

        LOGGER.info("User with id " + userId);
        if (userId == null || accountService.getUser(userId) == null) {
            // TODO
            // throw new AuthenticationException("Only authenticated users allowed to play a game");
            LOGGER.error("Only authenticated users allowed to play a game!");
            return;
        }

        LOGGER.info("Start handling message");

        final Message message;
        try {
            final ObjectNode node = objectMapper.readValue(textMessage.getPayload(), ObjectNode.class);
            message = new Message(node.get("type").asText(), node.get("data").toString());
            LOGGER.info("message parsed: type: " + message.getType() + ", data: " + message.getData());
            // TODO why doesn't working
//            message = objectMapper.readValue(textMessage.getPayload(), Message.class);
        } catch (JsonParseException | JsonMappingException e) {
            LOGGER.error("Couldn't parse JSON, message: " + textMessage.getPayload());
            return;
        } catch (IOException e) {
            LOGGER.error("Some problems while parsing message", e);
            return;
        }

        try {
            LOGGER.info("start handling message for user " + userId);
            messageHandlerContainer.handle(message, userId);
            LOGGER.info("end handling message");
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getType() + " with content: " + message.getData(), e);
        }
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession webSocketSession,
                                     @NotNull Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession webSocketSession,
                                      @NotNull CloseStatus closeStatus) throws Exception {
        LOGGER.info("ConnectionClosed");
        final Long userId = (Long) webSocketSession.getAttributes().get(USER_ID);
        if (userId == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }

        if (remotePointService.contains(webSocketSession)) {
            remotePointService.removeUser(userId);

            final Message message = new Message(Disconnect.Request.class, "{}");
            try {
                messageHandlerContainer.handle(message, userId);
            } catch (HandleException e) {
                LOGGER.error("Can't remove user from game");
            }
        }
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void sendIdToClient(@NotNull WebSocketSession session, Long id) {
        LOGGER.info("sendIdToClient, id = " + id + ", session: " + session);
        final Message message = new Message(Message.INITIALIZE_USER, String.valueOf(id));
        try {
            final String json = objectMapper.writeValueAsString(message);
            LOGGER.info("sendIdToClient, json: " + json);
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            LOGGER.error("Failed to send ID to user");
        }
    }

    // TODO check if needed
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}


