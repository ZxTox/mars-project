package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import com.stripe.model.checkout.Session;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private static final String TICKET_CLASS_PROPERTY = "ticketClass";
    private static final String IS_SINGLE_WAY_PROPERTY = "isSingleWay";
    public static final String PASSENGERS_FIELD_PROPERTY_NAME = "passengers";

    private Response() { }


    public static void sendCreatedCheckoutSession(RoutingContext ctx, Session session) {
        sendBookingMetadata(ctx, session);
    }

    public static void sendRetrievedCheckoutSession(RoutingContext ctx, Session session) {
        sendBookingMetadata(ctx, session);
    }

    private static void sendBookingMetadata(RoutingContext ctx, Session session) {
        JsonArray metadata = new JsonArray(session.getMetadata().get(PASSENGERS_FIELD_PROPERTY_NAME));
        sendJsonResponse(ctx, 201, new JsonObject()
                .put("id", session.getId())
                .put("totalPrice", (double) session.getAmountTotal() / 100)
                .put("quantity", metadata.size())
                .put("pricePerPerson", ((double) session.getAmountTotal() / metadata.size()) / 100)
                .put(TICKET_CLASS_PROPERTY, session.getMetadata().get(TICKET_CLASS_PROPERTY))
                .put("currency", session.getCurrency().toUpperCase())
                .put("class", metadata)
                .put(IS_SINGLE_WAY_PROPERTY, Boolean.valueOf(session.getMetadata().get(IS_SINGLE_WAY_PROPERTY)))
                .put("success", true)
        );
    }

    public static void sendUsers(RoutingContext ctx, List<String> users) {
        sendJsonResponse(ctx, 200, new JsonObject().put("users", new JsonArray(users)));
    }

    public static void sendSeats(RoutingContext ctx, List<String> bookedSeats) {
        sendJsonResponse(ctx, 200, new JsonObject().put("seats", new JsonArray(bookedSeats)));
    }

    public static void sendFlights(RoutingContext ctx, List<Flight> flights) {
        sendJsonResponse(ctx, 200, new JsonObject().put("flights", new JsonArray(flights)));
    }

    public static void sendBookings(RoutingContext ctx, List<Booking> bookings) {
        JsonArray array = new JsonArray();

        bookings.forEach(booking ->
            array.add(new JsonObject()
                    .put("bookingId", booking.getStripeId())
                    .put("price", (double) (booking.getPricePerPassenger() * booking.getPassengerList().size()) / 100)
                    .put(TICKET_CLASS_PROPERTY, booking.getTicketClass())
                    .put(IS_SINGLE_WAY_PROPERTY, booking.isSingleWay())
                    .put("flight", booking.getFlight())
                    .put(PASSENGERS_FIELD_PROPERTY_NAME, booking.getPassengerList())
                    .put("paymentStatus", booking.getPaymentStatus())
                    .put("createdAt", booking.getCreatedAt())
            ));


        sendJsonResponse(ctx, 200, new JsonObject().put("bookings", array));
    }

    public static void sendBookingData(RoutingContext ctx, Map<String, JsonObject> bookings) {
        JsonArray array = new JsonArray();

        bookings.forEach((flightId, value) ->
            array.add(new JsonObject()
                    .put("date", value.getString("date"))
                    .put("flightId", flightId)
                    .put("destinationPlanet", value.getString("destination"))
                    .put("estimatedArrival", value.getString("estimatedArrival"))
                    .put("bookedSeats", value.getInteger("bookedSeats"))
                    .put("maxSeats", 200)
                    .put(PASSENGERS_FIELD_PROPERTY_NAME, value.getJsonObject(PASSENGERS_FIELD_PROPERTY_NAME))
            )
        );

        sendJsonResponse(ctx, 200, new JsonObject().put("bookings", array));
    }


    public static void sendWebhookResponse(RoutingContext ctx) {
        sendJsonResponse(ctx, 200, new JsonObject()
                .put("success", true)
        );
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }
}
