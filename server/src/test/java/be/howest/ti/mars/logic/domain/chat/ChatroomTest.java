package be.howest.ti.mars.logic.domain.chat;

import be.howest.ti.mars.logic.domain.chat.events.*;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatroomTest {

    Chatroom chatroom;
    String clientId;

    @BeforeEach
    void setup() {
        chatroom = new Chatroom();
        clientId = "RANDOM_CLIENT_ID";
    }

    @Test
    void testJoinEvent() {
        String username = "RANDOM_USERNAME";
        String clientId = "RANDOM_CLIENT_ID";
        JoinEvent event = new JoinEvent(clientId, username);


        assertEquals(username, event.getUsername());
        assertEquals(clientId, event.getClientId());
    }


    @Test
    void testMessageEvent() {
        MessageEvent messageEvent = new MessageEvent(clientId, "Hello!");

        assertEquals("Hello!", messageEvent.getMessage());
        assertEquals(clientId, messageEvent.getClientId());
    }

    @Test
    void testEventType() {
        Event event = new Event(EventType.MESSAGE);

        assertEquals(EventType.MESSAGE, event.getType());
    }

    @Test
    void testOutgoingEvent() {
        BroadcastEvent broadcastEvent = new BroadcastEvent("Hello");

        assertEquals("Hello", broadcastEvent.getMessage());
        assertEquals(EventType.BROADCAST, broadcastEvent.getType());
    }

    @Test
    void testDiscardEvent() {
        DiscardEvent discardEvent = new DiscardEvent("RANDOM_ID");

        assertEquals("RANDOM_ID", discardEvent.getClientId());
        assertEquals(EventType.DISCARD, discardEvent.getType());
    }

    @Test
    void testEventFactoryBroadcastEvent() {
        EventFactory eventFactory = EventFactory.getInstance();

        BroadcastEvent broadcastEvent = eventFactory.createBroadcastEvent("Hello");

        assertEquals("Hello", broadcastEvent.getMessage());
        assertEquals(EventType.BROADCAST, broadcastEvent.getType());
    }

    @Test
    void testEventFactoryIncomingEvent() {
        EventFactory eventFactory = EventFactory.getInstance();
        JsonObject json = new JsonObject();
        json.put("type", "message");
        json.put("clientId", "RANDOM_ID");
        json.put("message", "Hello");


        MessageEvent incomingEvent = (MessageEvent) eventFactory.createIncomingEvent(json);

        assertEquals("RANDOM_ID", incomingEvent.getClientId());
        assertEquals("Hello", incomingEvent.getMessage());
        assertEquals(EventType.MESSAGE, incomingEvent.getType());
    }

    @Test
    void testEventFactoryJoinEvent() {
        EventFactory eventFactory = EventFactory.getInstance();
        JsonObject json = new JsonObject();
        json.put("type", "join");
        json.put("clientId", "RANDOM_ID");
        json.put("username", "User");


        JoinEvent incomingEvent = (JoinEvent) eventFactory.createIncomingEvent(json);

        assertEquals("RANDOM_ID", incomingEvent.getClientId());
        assertEquals("User", incomingEvent.getUsername());
        assertEquals(EventType.JOIN, incomingEvent.getType());
    }

    @Test
    void testEventFactoryUnicastEvent() {
        EventFactory eventFactory = EventFactory.getInstance();

        UnicastEvent unicastEvent = eventFactory.createUnicastEvent("User", "Hello");

        assertEquals("User", unicastEvent.getRecipient());
        assertEquals("Hello", unicastEvent.getMessage());
    }

    @Test
    void testEventFactoryDiscardEvent() {
        EventFactory eventFactory = EventFactory.getInstance();
        JsonObject json = new JsonObject();
        json.put("type", "INVALID");
        json.put("clientId", "RANDOM_ID");
        json.put("username", "User");

        IncomingEvent incomingEvent  = eventFactory.createIncomingEvent(json);

        assertEquals(EventType.DISCARD, incomingEvent.getType());
    }

    @Test
    void testOutgoingEventChatroom() {
        Chatroom chatroom = new Chatroom();


        JoinEvent joinEvent = new JoinEvent("RANDOM_ID", "User");
        BroadcastEvent outgoingEvent = (BroadcastEvent) chatroom.handleEvent(joinEvent);

        assertEquals("User", joinEvent.getUsername());
        assertEquals("RANDOM_ID", joinEvent.getClientId());
        assertEquals(EventType.BROADCAST, outgoingEvent.getType());
    }

    @Test
    void testMessageEventChatroom() {
        Chatroom chatroom = new Chatroom();

        MessageEvent messageEvent = new MessageEvent("RANDOM_ID", "Hello");
        UnicastEvent outgoingEvent = (UnicastEvent) chatroom.handleEvent(messageEvent);

        assertEquals("Hello", messageEvent.getMessage());
        assertEquals("RANDOM_ID", messageEvent.getClientId());
        assertEquals(EventType.UNICAST, outgoingEvent.getType());
    }

    @Test
    void testMessageEventChatroomBroadcast() {
        Chatroom chatroom = new Chatroom();

        JoinEvent joinEvent = new JoinEvent("RANDOM_ID", "User");
        BroadcastEvent broadcastEvent = (BroadcastEvent) chatroom.handleEvent(joinEvent);

        MessageEvent messageEvent = new MessageEvent("RANDOM_ID", "Hello");
        BroadcastEvent outgoingEvent = (BroadcastEvent) chatroom.handleEvent(messageEvent);

        assertEquals("Hello", messageEvent.getMessage());
        assertEquals("RANDOM_ID", messageEvent.getClientId());
        assertEquals(EventType.BROADCAST, outgoingEvent.getType());
    }

    @Test
    void testGetUsers() {
        Chatroom chatroom = new Chatroom();


        JoinEvent joinEvent = new JoinEvent("RANDOM_ID", "User");
        BroadcastEvent outgoingEvent = (BroadcastEvent) chatroom.handleEvent(joinEvent);

        assertEquals("User", chatroom.getAllUsernames().get(0));
    }
}