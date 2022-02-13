package be.howest.ti.mars.logic.domain.chat.events;

public class JoinEvent extends IncomingEvent{

    private final String username;

    public JoinEvent(String clientId, String username) {
        super(EventType.JOIN, clientId);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}

