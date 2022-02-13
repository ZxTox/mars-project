package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import be.howest.ti.mars.logic.domain.StripePayment;
import be.howest.ti.mars.logic.domain.chat.Chatroom;
import be.howest.ti.mars.logic.domain.chat.events.*;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * It should NOT be aware that it is used in the context of a webserver:
 * <p>
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Do not be afraid to create your own Java classes if needed.
 * <p>
 * Note: Json and JsonObject can (and should) be used in the web-package however.
 * <p>
 * (please update these comments in the final version)
 */
public class DefaultMarsController implements MarsController {

    private static final String CHNL_TO_CLIENTS = "events.to.clients";
    private static final String CHNL_TO_CLIENT_UNICAST = "events.to.client.";

    public static final Chatroom CHATROOM = new Chatroom();

    @Override
    public Session createCheckoutSession(Booking booking) {
        return StripePayment.getCheckoutSession(booking);
    }

    @Override
    public Session retrieveCheckoutSession(String sessionId) {
        return StripePayment.retrieveSession(sessionId);
    }

    @Override
    public void addBooking(Session session, Booking booking) {
        Repositories.getH2Repo().insertBooking(session, booking);
    }

    @Override
    public void updatePaymentStatus(String bookingId) {

        if (StringUtils.isBlank(bookingId))
            throw new IllegalArgumentException("No quote provided!");

        Repositories.getH2Repo().updateBookingPaymentStatus(bookingId);
    }

    @Override
    public List<String> getSeats(String flightId) {
        return Repositories.getH2Repo().getSeats(flightId);
    }

    @Override
    public Flight getFlight(String flightId) {
        return Repositories.getH2Repo().getFlightById(flightId);
    }

    @Override
    public List<Flight> getFlights(List<String> filter) {
        return Repositories.getH2Repo().getFlights(filter);
    }

    @Override
    public List<Booking> getBookings() {
       return Repositories.getH2Repo().getBookings();
    }

    @Override
    public Map<String, JsonObject> getBookingsData() {
        return Repositories.getH2Repo().getBookingsData();
    }


    @Override
    public Event constructEventFromWebhook(String payload, String sigHeader) {
        return StripePayment.constructEventFromWebhook(payload, sigHeader);
    }

    public void handleIncomingMessage(Message<JsonObject> msg, EventBus eb) {
        IncomingEvent incoming = EventFactory.getInstance().createIncomingEvent(msg.body());
        OutgoingEvent result = CHATROOM.handleEvent(incoming);
        handleOutgoingMessage(result, eb);
    }

    public void handleOutgoingMessage(OutgoingEvent result, EventBus eb) {
        switch (result.getType()) {
            case BROADCAST:
                broadcastMessage((BroadcastEvent) result, eb);
                break;
            case UNICAST:
                unicastMessage((UnicastEvent) result, eb);
                break;
            default:
                throw new IllegalArgumentException("Type of outgoing message not supported");
        }
    }

    public void broadcastMessage(BroadcastEvent e, EventBus eb) {
        eb.publish(CHNL_TO_CLIENTS, e.getMessage());
    }

    public void unicastMessage(UnicastEvent e, EventBus eb) {
        eb.publish(CHNL_TO_CLIENT_UNICAST + e.getRecipient(), e.getMessage());
    }
}