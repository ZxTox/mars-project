package be.howest.ti.mars.logic.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    @Test
    void getDifferenceInMonths() {
        // Arrange
        Flight flight = new Flight("Mars", "2052-02-11 20:30", "2054-04-11 20:30");

        // Act
        long difference = flight.getTravelTime();

        // Assert (difference should be 26 months)
        assertEquals(26, difference);
    }


    @Test
    void testGetters() {
        // Arrange
        Flight flight = new Flight("Mars", "2052-02-11 20:30", "2054-04-11 20:30");

        // Assert + Act
        assertEquals(String.join("T", "2052-02-11 20:30".split(" ")), flight.getDepartTime());
        assertEquals(String.join("T", "2054-04-11 20:30".split(" ")), flight.getEstimatedArrival());
        assertEquals("Mars", flight.getDestinationPlanet());
    }

}