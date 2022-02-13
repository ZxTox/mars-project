package be.howest.ti.mars.logic.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    Flight flight;

    @BeforeEach
    void getFlight() {
        // String flightId, String destinationPlanet, LocalDateTime departTime, LocalDateTime estimatedArrival
        Flight flight = new Flight("TEST7", "Mars", "2052-02-15 20:00", "2052-02-15 20:00");
    }

    @Test
    void invalidClassBooking() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Booking("NOT_A_VALID_CLASS", true, flight));
    }

    @Test
    void RoundTripTwiceAsExpensiveAsSingleWay() {
        // Arrange + Act + Assert
        assertEquals(259840 * 2, new Booking("economic", false, flight).getPricePerPassenger());
    }


    @Test
    void testBookingMetadata() {
        Booking booking = new Booking("ROYAL", true, flight);
        booking.setStripeId("STRIPE_ID");
        booking.setCreatedAt("2021-12-21 11:41");
        booking.setPaymentStatus("PAID");

        assertEquals("STRIPE_ID", booking.getStripeId());
        assertEquals("2021-12-21 11:41", booking.getCreatedAt());
        assertEquals("PAID", booking.getPaymentStatus());
        assertEquals(flight, booking.getFlight());
    }
}