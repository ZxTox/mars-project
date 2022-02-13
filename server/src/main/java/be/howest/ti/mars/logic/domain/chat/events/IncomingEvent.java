package be.howest.ti.mars.logic.domain.chat.events;

public abstract class IncomingEvent extends Event{

    private final String clientId;

    protected IncomingEvent(EventType type, String clientId) {
        super(type);
        this.clientId = clientId;
    }


    public String getClientId() {
        return clientId;
    }

}
