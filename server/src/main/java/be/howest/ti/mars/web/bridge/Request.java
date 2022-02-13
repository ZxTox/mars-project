package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import be.howest.ti.mars.logic.domain.Passenger;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 */
public class Request {
    private static final Logger LOGGER = Logger.getLogger(Request.class.getName());
    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public String getFlightId() {
        return params.body().getJsonObject().getString("flightId");
    }

    public String getFlightIdPath() {
        return params.pathParameter("flightId").getString();
    }

    public Booking getBookingMetaData(Flight flight) {
        JsonObject objectBody = params.body().getJsonObject();

        if (objectBody.getJsonArray("passengers").isEmpty()) {
            LOGGER.log(Level.INFO, "No given passengers booking!");
            throw new IllegalArgumentException("No passengers!");
        }

        Booking booking = new Booking(objectBody.getString("class"),objectBody.getBoolean("isSingleWay"), flight);

        objectBody.getJsonArray("passengers").stream().forEach(p -> {
            JsonObject passenger = JsonObject.mapFrom(p);
            booking.addPassenger(new Passenger(passenger.getString("marsId"), passenger.getString("seat")));
        });

        return booking;
    }

    public String getSessionId() {
        return params.pathParameter("sessionId").getString();
    }

    public String getWebhookBodyPayload() {
        return params.body().toString();
    }

    public List<String> getDateFilter() {
        if (params.queryParameter("date") == null) {
            return Collections.emptyList();
        }
        return List.of(params.queryParameter("date").toString(), params.queryParameter("destination").toString().toUpperCase());
    }

    public String getSigHeader() {
        return params.headerParameter(params.headerParametersNames().get(0)).toString();
    }

}
