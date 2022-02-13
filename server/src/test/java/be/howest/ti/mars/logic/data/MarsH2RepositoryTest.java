package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import com.stripe.model.checkout.Session;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url","jdbc:h2:~/mars-db",
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getFlight() {
        // Arrange
        String id = "X7X7";

        // Act
        Flight flight = Repositories.getH2Repo().getFlightById(id);

        // Assert
        Assertions.assertTrue(flight != null && !StringUtil.isNullOrEmpty(flight.getFlightId()));
    }

    @Test
    void getSeats() {
        // Arrange
        String flightId = "X7X7";

        // Act
        List<String> bookedSeats = Repositories.getH2Repo().getSeats(flightId);

        // Assert
        Assertions.assertTrue(bookedSeats.size() >= 1);
    }

    @Test
    void getFlights() {
        // Arrange + act
        List<Flight> allFlights = Repositories.getH2Repo().getFlights(Collections.emptyList());

        // Assert
        Assertions.assertTrue(allFlights.size() > 1);
    }

    @Test
    void getFlightsWithFilters() {
        // Arrange + act
        List<String> filters = List.of("2052-02-15", "MARS");
        List<Flight> allFlights = Repositories.getH2Repo().getFlights(filters);

        // Assert
        Assertions.assertTrue(allFlights.size() >= 1);
        Assertions.assertEquals("MARS", allFlights.get(0).getDestinationPlanet());
    }

    @Test
    void getBookings() {
        // Arrange + act
        List<Booking> allBookings = Repositories.getH2Repo().getBookings();

        // Assert
        Assertions.assertTrue(allBookings.size() >= 1);
    }

    @Test
    void testAddPassenger() {
        // Arrange
        Flight flight = new Flight("X7X7", "MARS", "2021-12-22 20:30", "2022-01-22 20:30");
        Booking booking = new Booking("ROYAl", true, flight);
        Passenger passenger = new Passenger("RANDOM_ID", "C7");
        booking.addPassenger(passenger);
        Session session = StripePayment.getCheckoutSession(booking);

        // Act
        Repositories.getH2Repo().insertBooking(session, booking);

        List<Passenger> passengerList = Repositories.getH2Repo().getPassengers(session.getId());

        Assertions.assertEquals(passenger, passengerList.get(0));
    }

    @Test
    void testALlFlightPassengers() {
        Map<String, Map<String, Integer>> passengers = Repositories.getH2Repo().getFlightPassengers("X7X7");

        assertNotNull(passengers.get("ROYAL"));
    }


    @Test
    void testGetBookingDataPublic() {
        Map<String, JsonObject> bookingsData = Repositories.getH2Repo().getBookingsData();

        assertTrue(bookingsData.containsKey("X7X7"));
    }
}
