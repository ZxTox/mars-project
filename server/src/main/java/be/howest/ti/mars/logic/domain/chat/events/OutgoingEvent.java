package be.howest.ti.mars.logic.domain.chat.events;

public abstract class OutgoingEvent extends Event{

    private String message;

    protected OutgoingEvent(EventType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
