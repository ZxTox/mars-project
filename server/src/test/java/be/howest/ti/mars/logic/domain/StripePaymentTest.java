package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.exceptions.InvalidHeaderSignatureException;
import be.howest.ti.mars.logic.exceptions.PaymentCreateCheckoutException;
import be.howest.ti.mars.logic.exceptions.PaymentRetrieveCheckoutException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StripePaymentTest {

    Flight flight;

    @BeforeEach
    void getFlight() {
        // String flightId, String destinationPlanet, LocalDateTime departTime, LocalDateTime estimatedArrival
        flight = new Flight("TEST7", "Mars", "2052-02-15 20:00", "2052-02-15 20:00");
    }

    Session createBooking() {
        // Arrange
        Booking booking = new Booking("royal", true, flight);
        booking.addPassenger(new Passenger("RANDOM_MARS_ID_7777", "C7"));

        // Act (takes 1-2 seconds)
        return StripePayment.getCheckoutSession(booking);
    }

    @Test
    void createCheckoutSessionUrlIsNotNull() {
        Session session = createBooking();

        // Assert
        assertNotNull(session.getUrl());
        assertNotNull(session.getId());
    }

    @Test
    void testRetrieveCheckoutSession() {
        Session session = createBooking();

        Session retrievedSession = StripePayment.retrieveSession(session.getId());

        assertNotNull(retrievedSession.getUrl());
        assertEquals(session.getId(), retrievedSession.getId());
    }

    @Test
    void invalidCheckoutSession() {
        Booking booking = new Booking("ROYAL", true, flight);
        assertThrows(PaymentCreateCheckoutException.class, () -> StripePayment.getCheckoutSession(booking));
    }

    @Test
    void invalidWebhookSignature() {
        // Arrange + Act + Assert
        assertThrows(InvalidHeaderSignatureException.class, () ->
                StripePayment.constructEventFromWebhook("{'test': 'test'}", "INVALID_SIG_HEADER")
        );
    }

    @Test
    void invalidSessionId() {
       assertThrows(PaymentRetrieveCheckoutException.class, () -> StripePayment.retrieveSession("INVALID_SESSION_Id"));
    }

    @Test
    void testStripeParams() {
        // Arrange
        Booking booking = new Booking("royal", true, flight);
        Booking bookingRoundTrip = new Booking("royal", false, flight);
        booking.addPassenger(new Passenger("RANDOM_MARS_ID_7777", "C7"));


        // Assert
        assertEquals(Currency.EUR.toString(), booking.getStripeObject().get("currency"));
        assertEquals("Single-way to Mars", booking.getStripeObject().get("description"));
        assertEquals("Round-trip to Mars", bookingRoundTrip.getStripeObject().get("description"));
    }

}