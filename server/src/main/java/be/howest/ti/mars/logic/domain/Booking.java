package be.howest.ti.mars.logic.domain;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.*;

public class Booking {

    private final String ticketClass;
    private final boolean isSingleWay;
    private final List<Passenger> passengerList;
    private final Flight flight;


    private String stripeId;
    private String createdAt;
    private String paymentStatus;


    public Booking(String ticketClass, Boolean isSingleWayFlight, Flight flight) {
        if(ticketClass.isEmpty() || !EnumUtils.isValidEnum(TicketClass.class, ticketClass.toUpperCase())) {
            throw new IllegalArgumentException("Invalid ticket class!");
        }
        this.flight = flight;
        this.ticketClass = ticketClass;
        this.passengerList = new ArrayList<>();
        this.isSingleWay = isSingleWayFlight;
    }

    public void addPassenger(Passenger passenger) {
        passengerList.add(passenger);
    }

    public Flight getFlight() {
        return flight;
    }

    public int getPricePerPassenger() {
        int price = 259840; // economic price in cents since Stripe uses cents

        if (ticketClass.equalsIgnoreCase(TicketClass.ROYAL.toString())) {
            price = 475840;
        }else if(ticketClass.equalsIgnoreCase(TicketClass.BUSINESS.toString())) {
            price = 331840;
        }

        return isSingleWay ? price : price * 2;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<Passenger> getPassengerList() {
        return Collections.unmodifiableList(passengerList);
    }

    public Boolean isSingleWay() {
        return isSingleWay;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public JsonArray getPassengersMetadata() {
        JsonArray res = new JsonArray();
        getPassengerList().forEach(passenger -> res.add(JsonObject.mapFrom(passenger)));
        return res;
    }

    public Map<String, Object> getStripeObject() {
        Map<String, Object> stripeParams = new HashMap<>();

        stripeParams.put("name", String.format("%s ticket MarsMile", StringUtils.capitalize(ticketClass)));
        stripeParams.put("description", isSingleWay ? "Single-way to Mars" : "Round-trip to Mars");
        stripeParams.put("amount", getPricePerPassenger());
        stripeParams.put("currency", StripePayment.CURRENCY_PREFERENCE);
        stripeParams.put("quantity", getPassengerList().size());

        return stripeParams;
    }

}
