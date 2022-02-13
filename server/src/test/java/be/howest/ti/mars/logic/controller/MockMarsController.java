package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import be.howest.ti.mars.logic.domain.Passenger;
import be.howest.ti.mars.logic.domain.chat.events.BroadcastEvent;
import be.howest.ti.mars.logic.domain.chat.events.OutgoingEvent;
import be.howest.ti.mars.logic.domain.chat.events.UnicastEvent;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

public class MockMarsController implements MarsController {

    @Override
    public Session createCheckoutSession(Booking booking) {
        return null;
    }

    @Override
    public Session retrieveCheckoutSession(String sessionId) {
        return null;
    }

    @Override
    public void addBooking(Session session, Booking booking) {
    }

    @Override
    public void updatePaymentStatus(String bookingId) {

    }

    @Override
    public List<String> getSeats(String flightId) {
        return List.of("S7", "D7", "D9", "S8");
    }

    @Override
    public Flight getFlight(String flightId) {
        return  new Flight(flightId, "MARS", "2021-12-16 20:30", "2022-01-16 20:30");
    }

    @Override
    public List<Flight> getFlights(List<String> filter) {
        return List.of(
                new Flight("RANDOM_7", "MARS", "2021-12-16 20:30", "2022-01-16 20:30"),
                new Flight("RANDOM_8", "MARS", "2021-12-16 20:30", "2022-01-16 20:30")
        );
    }

    @Override
    public List<Booking> getBookings() {
        return null;
    }

    @Override
    public Map<String, JsonObject> getBookingsData() {
        return null;
    }


    @Override
    public Event constructEventFromWebhook(String payload, String sigHeader) {
        return null;
    }


    @Override
    public void handleIncomingMessage(Message<JsonObject> msg, EventBus eb) {

    }

    @Override
    public void handleOutgoingMessage(OutgoingEvent result, EventBus eb) {

    }

    @Override
    public void broadcastMessage(BroadcastEvent e, EventBus eb) {

    }

    @Override
    public void unicastMessage(UnicastEvent e, EventBus eb) {

    }

}
