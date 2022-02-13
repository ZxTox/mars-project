package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import be.howest.ti.mars.logic.exceptions.MarsResourceNotFoundException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will find one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class,
 * pass it to the controller and use its result to generate a response in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }


    public void createCheckoutSession(RoutingContext ctx) {
        Request request = Request.from(ctx);

        Flight flight = controller.getFlight(request.getFlightId());

        Booking booking = request.getBookingMetaData(flight);

        Session session = controller.createCheckoutSession(booking);

        controller.addBooking(session, booking);

        Response.sendCreatedCheckoutSession(ctx, session);
    }
    public void retrieveCheckoutSession(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String sessionId = request.getSessionId();
        Session session = controller.retrieveCheckoutSession(sessionId);

        Response.sendRetrievedCheckoutSession(ctx, session);
    }

    private void getUsers(RoutingContext ctx) {
        List<String> users = MarsRtcBridge.CHATROOM.getAllUsernames();

        Response.sendUsers(ctx, users);
    }


    private void getSeats(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String flightId = request.getFlightIdPath();
        List<String> bookedSeats = controller.getSeats(flightId);

        Response.sendSeats(ctx, bookedSeats);
    }

    private void getFlights(RoutingContext ctx) {
        Request request = Request.from(ctx);

        List<String> dateFilter = request.getDateFilter();


        List<Flight> flights = controller.getFlights(dateFilter);

        Response.sendFlights(ctx, flights);
    }

    private void getBookings(RoutingContext ctx) {
       List<Booking> bookings = controller.getBookings();

       Response.sendBookings(ctx, bookings);
    }

    private void getBookingsData(RoutingContext ctx) {
        Map<String, JsonObject> passengerMap = controller.getBookingsData();
        Response.sendBookingData(ctx, passengerMap);
    }

    public void handleWebhookEvents(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String payload = request.getWebhookBodyPayload();
        String sigHeader = request.getSigHeader();


        Event event = controller.constructEventFromWebhook(payload, sigHeader);


        String bookingId = new JsonObject(payload).getJsonObject("data").getJsonObject("object").getString("id");

        if (event.getType().equals("checkout.session.completed")) {
            LOGGER.log(Level.INFO, "Checkout session completed for with id: {0}", event.getId());
            // Updating the booking by setting status to PAID
            controller.updatePaymentStatus(bookingId);
        }else {
            LOGGER.log(Level.INFO, "Unhandled event: {0}", event.getType());
            // TEMP CODE
        }

        Response.sendWebhookResponse(ctx);
    }


    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: createCheckoutSession");
        routerBuilder.operation("createCheckoutSession").handler(this::createCheckoutSession);

        LOGGER.log(Level.INFO, "Installing handler for: getUsers");
        routerBuilder.operation("getUsers").handler(this::getUsers);

        LOGGER.log(Level.INFO, "Installing handler for: getSeats");
        routerBuilder.operation("getSeats").handler(this::getSeats);

        LOGGER.log(Level.INFO, "Installing handler for: getFlights");
        routerBuilder.operation("getFlights").handler(this::getFlights);

        LOGGER.log(Level.INFO, "Installing handler for: getBookings");
        routerBuilder.operation("getBookings").handler(this::getBookings);

        LOGGER.log(Level.INFO, "Installing handler for: retrieveCheckoutSession");
        routerBuilder.operation("retrieveCheckoutSession").handler(this::retrieveCheckoutSession);

        LOGGER.log(Level.INFO, "Installing handler for: getBookingsData");
        routerBuilder.operation("getBookingsData").handler(this::getBookingsData);

        LOGGER.log(Level.INFO, "Installing handler for: handleWebhookEvents");
        routerBuilder.operation("handleWebhookEvents").handler(this::handleWebhookEvents);


        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }


    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        if (cause instanceof MarsResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")

                // Headers for sockets
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)

                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}