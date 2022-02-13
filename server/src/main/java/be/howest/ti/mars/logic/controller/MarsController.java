package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import be.howest.ti.mars.logic.domain.chat.events.BroadcastEvent;
import be.howest.ti.mars.logic.domain.chat.events.OutgoingEvent;
import be.howest.ti.mars.logic.domain.chat.events.UnicastEvent;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

public interface MarsController {

    Session createCheckoutSession(Booking booking);

    Session retrieveCheckoutSession(String sessionId);

    void addBooking(Session session, Booking booking);

    void updatePaymentStatus(String bookingId);

    List<String> getSeats(String flightId);

    Flight getFlight(String flightId);

    List<Flight> getFlights(List<String> filter);

    List<Booking> getBookings();

    Map<String, JsonObject> getBookingsData();

    Event constructEventFromWebhook(String payload, String sigHeader);

    void handleIncomingMessage(Message<JsonObject> msg, EventBus eb);

    void handleOutgoingMessage(OutgoingEvent result, EventBus eb);

    void broadcastMessage(BroadcastEvent e, EventBus eb);

    void unicastMessage(UnicastEvent e, EventBus eb);


}
