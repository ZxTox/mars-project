package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.exceptions.InvalidHeaderSignatureException;
import be.howest.ti.mars.logic.exceptions.PaymentCreateCheckoutException;
import be.howest.ti.mars.logic.exceptions.PaymentRetrieveCheckoutException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StripePayment {


    private static final Logger LOGGER = Logger.getLogger(StripePayment.class.getName());

    public static final String CURRENCY_PREFERENCE = Currency.EUR.toString();
    private static final String YOUR_DOMAIN = "http://127.0.0.1:5500/src"; // change to your front-end host & port
    private static final String SUCCESS_URL = String.format("%s/success.html?session_id={CHECKOUT_SESSION_ID}", YOUR_DOMAIN);
    private static final String CANCEL_URL = String.format("%s/cancel.html", YOUR_DOMAIN);
    private static final String ENDPOINT_SECRET = "whsec_2dCITYMDdHSrUqsybKf9JJDuxtOqrswR";
    private StripePayment(){
    }

    static {
        Stripe.apiKey = "sk_test_51JrQEKAi0vwMn6lUDf3VSg0yRJb7ZCCnjjlx1E4qvOxMbKaaBymRu8QXK88xzi2v6A4aKN0UQu8UVwyGGljhmV3P0073Nt7xvc";
    }

    public static Session getCheckoutSession(Booking booking) {
        try {
            return Session.create(getStripeParams(booking));
        } catch (StripeException ex) {
            LOGGER.log(Level.INFO, "Error retrieving the payment checkout session url.");
            throw new PaymentCreateCheckoutException("Error retrieving the payment checkout session url.");
        }

    }

    public static Session retrieveSession(String sessionId) {
        try {
            return Session.retrieve(sessionId);
        } catch (StripeException ex) {
            LOGGER.log(Level.INFO, "Error retrieving the session.");
            throw new PaymentRetrieveCheckoutException(String.format("Error retrieving the session. %s", ex.getStripeError().getMessage()));
        }
    }

    public static Event constructEventFromWebhook(String payload, String sigHeader) {
        try {
            return Webhook.constructEvent(payload, sigHeader, ENDPOINT_SECRET);
        } catch (SignatureVerificationException ex) {
            LOGGER.log(Level.INFO, "Invalid header signature. %s");
            throw new InvalidHeaderSignatureException("Invalid header signature.");
        }

    }

    private static List<Object> getPaymentMethods() {
        List<Object> paymentMethods = new ArrayList<>();
        paymentMethods.add(SessionCreateParams.PaymentMethodType.CARD);
        paymentMethods.add(SessionCreateParams.PaymentMethodType.BANCONTACT);
        paymentMethods.add(SessionCreateParams.PaymentMethodType.IDEAL);
        paymentMethods.add(SessionCreateParams.PaymentMethodType.SOFORT);
        paymentMethods.add(SessionCreateParams.PaymentMethodType.SEPA_DEBIT);
        paymentMethods.add(SessionCreateParams.PaymentMethodType.KLARNA);
        paymentMethods.add(SessionCreateParams.PaymentMethodType.P24);
        return paymentMethods;
    }

    private static Map<String, Object> getStripeParams(Booking booking) {
        return Map.of(
                "success_url", SUCCESS_URL,
                "cancel_url", CANCEL_URL,
                "payment_method_types", getPaymentMethods(),
                "line_items", List.of(booking.getStripeObject()),
                "mode", SessionCreateParams.Mode.PAYMENT,
                "metadata", Map.of("passengers", booking.getPassengersMetadata(), "isSingleWay", booking.isSingleWay(), "ticketClass", booking.getTicketClass())
        );
    }
}
