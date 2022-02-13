package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.exceptions.InvalidHeaderSignatureException;
import be.howest.ti.mars.logic.exceptions.PaymentRetrieveCheckoutException;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:~/mars-db",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getFlight() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertNotNull(sut.getFlight("X7X7"));
    }

    @Test
    void getSeats() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertTrue(sut.getSeats("X7X7").size() >= 1);
    }

    @Test
    void throwInvalidGetFlight() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(RepositoryException.class, () -> sut.getFlight("INVALID_ID"));
    }

    @Test
    void getBookings() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertNotNull(sut.getBookings());
    }

    @Test
    void invalidHeaderSignature() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(InvalidHeaderSignatureException.class, () -> sut.constructEventFromWebhook("{'test': 'test'}", "INVALID_SIG_HEADER"));
    }

    @Test
    void emptyBookingId() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updatePaymentStatus(""));
    }

    @Test
    void invalidRetrieveCheckoutSession() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(PaymentRetrieveCheckoutException.class, () -> sut.retrieveCheckoutSession("INVALID_SESSION"));
    }

}
