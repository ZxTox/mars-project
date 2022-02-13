package be.howest.ti.mars.logic.domain;


import java.util.Objects;
import java.util.UUID;

public class Passenger {

    private final String marsId;
    private final String seat;
    private final String uniqueTicketNumber;

    public Passenger(String marsId, String seat, String ticketNumber) {
        this.marsId = marsId;
        this.seat = seat;
        this.uniqueTicketNumber = ticketNumber;
    }

    public Passenger(String marsId, String seat) {
        this(marsId, seat, getRandomNumber());
    }

    private static String getRandomNumber() {
        return UUID.randomUUID().toString();
    }


    public String getMarsId() {
        return marsId;
    }

    public String getSeat() {
        return seat;
    }

    public String getUniqueTicketNumber() {
        return uniqueTicketNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(marsId, passenger.marsId) && Objects.equals(uniqueTicketNumber, passenger.uniqueTicketNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marsId, uniqueTicketNumber);
    }
}
