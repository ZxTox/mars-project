package be.howest.ti.mars.logic.domain.chat;

import be.howest.ti.mars.logic.domain.chat.events.*;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chatroom {

    private static final String ADMIN_NAME = "ADMIN";
    private final Map<String, String> users = new HashMap<>();

    public OutgoingEvent handleEvent(IncomingEvent e) {
        OutgoingEvent result = null;

        if(e.getType().equals(EventType.JOIN)){
            result = handleJoinEvent((JoinEvent) e);
        }else if(e.getType().equals(EventType.MESSAGE)) {
            result = handleMessageEvent((MessageEvent) e);
        }
        return result;
    }

    private String generateOutgoingMessageObject(String from, String message) {
        JsonObject outgoingMessageObj = new JsonObject();

        outgoingMessageObj.put("from", from).put("msg", message);

        return outgoingMessageObj.toString();
    }

    private String generateOutgoingMessageObject(String from, String message, String clientId) {
        JsonObject outgoingMessageObj = new JsonObject();

        outgoingMessageObj.put("from", from).put("msg", message).put("clientId", clientId);

        return outgoingMessageObj.toString();
    }

    private OutgoingEvent handleJoinEvent(JoinEvent e) {
        String username = e.getUsername();
        if (users.containsValue(username)) {
            String outgoingMessage = generateOutgoingMessageObject(ADMIN_NAME,
                    String.format("The username %s has already been chosen", username));
            return EventFactory.getInstance().createUnicastEvent(e.getClientId(), outgoingMessage);
        }

        String outgoingMessage = "";
        if (users.containsKey(e.getClientId())) {

            outgoingMessage = generateOutgoingMessageObject(ADMIN_NAME,
                    String.format("User %s is renamed to %s", users.get(e.getClientId()), username));
        } else {
            outgoingMessage = generateOutgoingMessageObject(ADMIN_NAME,
                    String.format("User %s joined the room", username));
        }

        users.put(e.getClientId(), username);
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }

    private OutgoingEvent handleMessageEvent(MessageEvent e) {
        if (!users.containsKey(e.getClientId())) {
            String outgoingMessage = generateOutgoingMessageObject("ADMIN - ME",
                    "Failed to send message - please choose a (new) username");
            return EventFactory.getInstance().createUnicastEvent(e.getClientId(), outgoingMessage);
        }

        String username = users.get(e.getClientId());

        String outgoingMessage = generateOutgoingMessageObject(username,
                e.getMessage(), e.getClientId());

        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }

    public List<String> getAllUsernames() {
        return new ArrayList<>(users.values());
    }

}