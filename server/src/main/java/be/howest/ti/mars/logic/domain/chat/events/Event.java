package be.howest.ti.mars.logic.domain.chat.events;

public class Event {
    private EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
