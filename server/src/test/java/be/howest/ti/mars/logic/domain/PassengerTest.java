package be.howest.ti.mars.logic.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerTest {

    @Test
    void testPassengerEquals() {
        // String marsId, String seat, String ticketNumber
        Passenger p1 = new Passenger("RANDOM_ID", "C7", "RANDOM_TICKET");
        Passenger p2 = new Passenger("RANDOM_ID", "C7", "RANDOM_TICKET");

        assertEquals(p1, p2);
    }

    @Test
    void testNotEquals() {
        Passenger p1 = new Passenger("RANDOM_ID", "C7", "RANDOM_TICKET1");
        Passenger p2 = new Passenger("RANDOM_ID2", "C7", "RANDOM_TICKET4");

        assertNotEquals(p1, p2);
    }
}