package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Booking;
import be.howest.ti.mars.logic.domain.Flight;
import be.howest.ti.mars.logic.domain.Passenger;
import be.howest.ti.mars.logic.domain.TicketClass;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import com.stripe.model.checkout.Session;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start-project there was no need for an Interface MarsRepository.
Please always use interfaces when needed.
To make this class useful, please complete it with the topics seen in the module OOA & SD
- Make sure the conf/config.json (local & production) properties are correct.
- The h2 web console is available at http://localhost:9000
- The h2 database file is located at ~/mars-db
- Don't create tables manually but create sql files in the folder resources.
  - With each deploy create the db structure from scratch (it's just a poc).
  - Two starter sql script are already given.
- Hint:
  - Mocking this repository for testing purposes is not needed.
    Create database creating and population scripts in plain SQL (resources folder).
    Use the @Before or @BeforeEach (depending on the type of test) to quickly create a populated database.
 */
public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());

    private static final String SQL_INSERT_BOOKING = "insert into bookings(booking_id, is_single_way, price, flight) values(?, ?, ?, ?)";
    private static final String SQL_INSERT_PASSENGER = "insert into bookingPassengers(booking_id, ticket_number, mars_id, seat, ticket_class) values (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_BOOKING_STATUS = "update bookings set status = ? where booking_id = ?";
    private static final String ENUM_PAID_BOOKING = "PAID";

    private static final String SQL_GET_FLIGHT_BY_ID = "select * from flights where flight_id = ?";
    private static final String SQL_GET_FLIGHTS = "select * from flights";
    private static final String SQL_GET_FLIGHTS_FILTER = "select * from flights where month(depart_time) = month(?) and year(depart_time) = year(?) and destination = ?";

    private static final String SQL_GET_BOOKINGS = "select * from bookings";
    private static final String SQL_GET_PASSENGERS_BOOKING = "select * from bookingpassengers where booking_id = ?";
    private static final String SQL_GET_FLIGHT_CLASS = "select ticket_class from bookingpassengers where booking_id = ? limit 1";

    private static final String SQL_GET_BOOKED_SEATS = "select bp.seat, b.FLIGHT from BOOKINGPASSENGERS BP\n" +
            "join BOOKINGS B on BP.BOOKING_ID = B.BOOKING_ID\n" +
            "where FLIGHT = ?";

    private static final String SQL_BOOKINGS_DATA = "select DEPART_TIME as date,\n" +
            "       FLIGHT as flight_id,\n" +
            "       DESTINATION as destinationPlanet,\n" +
            "       ESTIMATED_ARRIVAL as estimatedArrival,\n" +
            "       count(B2.TICKET_NUMBER) as bookedSeats\n" +
            "from BOOKINGS\n" +
            "join BOOKINGPASSENGERS B2 on BOOKINGS.BOOKING_ID = B2.BOOKING_ID\n" +
            "join FLIGHTS B on BOOKINGS.FLIGHT = B.FLIGHT_ID\n" +
            "group by ESTIMATED_ARRIVAL, FLIGHT, DESTINATION, DEPART_TIME";

    private static final String SQL_GET_FLIGHT_PASSENGERS = "select FLIGHT, TICKET_NUMBER, TICKET_CLASS, MARS_ID, SEAT from BOOKINGS\n" +
            "join BOOKINGPASSENGERS B on BOOKINGS.BOOKING_ID = B.BOOKING_ID\n" +
            "where flight = ? \n" +
            "group by FLIGHT, TICKET_NUMBER, TICKET_CLASS, MARS_ID, SEAT";


    public static final String FLIGHT_ID_PROPERTY_NAME = "flight_id";
    public static final String DESTINATION_PLANET_PROPERTY_NAME = "destination";

    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database webconsole started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    private void addPassengers(Session session) {
        JsonArray passengers = new JsonArray(session.getMetadata().get("passengers"));
        passengers.stream().forEach(p -> {
            JsonObject passengerObj = JsonObject.mapFrom(p);
            Passenger passenger = new Passenger(
                    passengerObj.getString("marsId"),
                    passengerObj.getString("seat"),
                    passengerObj.getString("uniqueTicketNumber"));
            addPassenger(session, passenger);
        });
    }

    public Flight getFlightById(String flightId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_FLIGHT_BY_ID)
        ) {
            stmt.setString(1, flightId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new RepositoryException("No flight corresponds to that id.");
                }
                return new Flight(rs.getString(FLIGHT_ID_PROPERTY_NAME),
                        rs.getString(DESTINATION_PLANET_PROPERTY_NAME),
                        rs.getTimestamp("depart_time").toLocalDateTime(),
                        rs.getTimestamp("estimated_arrival").toLocalDateTime()
                );
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get flight.", ex);
            throw new RepositoryException("Could not get flight.");
        }
    }

    public List<String> getSeats(String flightId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_BOOKED_SEATS)
        ) {

            stmt.setString(1, flightId);

            ResultSet rs = stmt.executeQuery();

            List<String> bookedSeats = new ArrayList<>();

            while(rs.next()) {
                bookedSeats.add(rs.getString("seat"));
            }

            return bookedSeats;


        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch seats.", ex);
            throw new RepositoryException("Could not fetch seats.");
        }
    }

    public List<Flight> getFlights(List<String> filter) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(filter.isEmpty() ? SQL_GET_FLIGHTS : SQL_GET_FLIGHTS_FILTER)
        ) {

            if (!filter.isEmpty()) {
                stmt.setString(1, filter.get(0));
                stmt.setString(2, filter.get(0));
                stmt.setString(3, filter.get(1));
            }

            ResultSet rs = stmt.executeQuery();

            List<Flight> allFlights = new ArrayList<>();

            while(rs.next()) {
                String flightId = rs.getString(FLIGHT_ID_PROPERTY_NAME);
                String destination = rs.getString(DESTINATION_PLANET_PROPERTY_NAME);
                LocalDateTime departTime = rs.getTimestamp("depart_time").toLocalDateTime();
                LocalDateTime estimatedArrival = rs.getTimestamp("estimated_arrival").toLocalDateTime();


                Flight flight = new Flight(flightId, destination, departTime, estimatedArrival);
                allFlights.add(flight);
            }

            return allFlights;


        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch flights.", ex);
            throw new RepositoryException("Could not fetch flights.");
        }
    }

    public List<Booking> getBookings() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_BOOKINGS)
        ) {


            ResultSet rs = stmt.executeQuery();

            List<Booking> allBookings = new ArrayList<>();

            while(rs.next()) {
                Flight flight = getFlightById(rs.getString("flight"));
                String bookingId = rs.getString("booking_id");
                boolean isSingleWay = rs.getBoolean("is_single_way");
                String paymentStatus = rs.getString("status");
                String createdAt = rs.getTimestamp("created_at").toLocalDateTime().toString();

                List<Passenger> passengers = getPassengers(bookingId);
                String ticketClass = getFlightClass(bookingId);

                Booking booking = new Booking(ticketClass, isSingleWay, flight);
                booking.setStripeId(bookingId);
                booking.setCreatedAt(createdAt);
                booking.setPaymentStatus(paymentStatus);

                passengers.forEach(booking::addPassenger);

                allBookings.add(booking);
            }

            return allBookings;


        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch bookings.", ex);
            throw new RepositoryException("Could not fetch bookings.");
        }
    }

    public List<Passenger> getPassengers(String bookingId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_PASSENGERS_BOOKING)
        ) {
            stmt.setString(1, bookingId);

            ResultSet rs = stmt.executeQuery();

            List<Passenger> passengers = new ArrayList<>();

            while(rs.next()) {
                String marsId = rs.getString("mars_id");
                String seat = rs.getString("seat");
                String ticketNumber = rs.getString("ticket_number");

                Passenger passenger = new Passenger(marsId, seat, ticketNumber);

                passengers.add(passenger);
            }

            return passengers;


        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch passengers by bookingId.", ex);
            throw new RepositoryException("Could not fetch passengers by bookingId.");
        }
    }

    public Map<String, Map<String, Integer>> getFlightPassengers(String flightId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_FLIGHT_PASSENGERS)
        ) {
            stmt.setString(1, flightId);

            ResultSet rs = stmt.executeQuery();

            Map<String, Map<String, Integer>> passengerMap = new HashMap<>();


            Arrays.stream(TicketClass.values()).forEach(ticketClass ->
                passengerMap.put(ticketClass.toString(),
                        new HashMap<>(Map.of(
                                "0-18", 0,
                                "19-28", 0,
                                "29-48", 0,
                                "49-65", 0,
                                "66-999", 0
                        )))
            );


            while(rs.next()) {
                String ticketNumber = rs.getString("ticket_number");
                String ticketClass = rs.getString("ticket_class");

                String randomAge = ticketNumber.replaceAll("\\D+","");
                int age = randomAge.length() < 2 ? 29 : Integer.parseInt(randomAge.substring(0, 2));


                passengerMap.get(ticketClass).keySet().forEach(key -> {
                    String[] splitAgeCategory = key.split("-");
                    if (age >= Integer.parseInt(splitAgeCategory[0]) && age <= Integer.parseInt(splitAgeCategory[1])) {
                        int previousValue = passengerMap.get(ticketClass).get(key);
                        passengerMap.get(ticketClass).put(key, previousValue + 1);
                    }
                });

            }

            return passengerMap;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch passengers.", ex);
            throw new RepositoryException("Could not fetch passengers.");
        }
    }

    public String getFlightClass(String bookingId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_FLIGHT_CLASS)
        ) {
            stmt.setString(1, bookingId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ticket_class");
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get flightClass.", ex);
            throw new RepositoryException("Could not get flightClass.");
        }
    }

    public Map<String, JsonObject> getBookingsData() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_BOOKINGS_DATA)
        ) {

            ResultSet rs = stmt.executeQuery();
            Map<String, JsonObject> bookingsData = new HashMap<>();

            while(rs.next()) {
                String flightId = rs.getString(FLIGHT_ID_PROPERTY_NAME);

                bookingsData.put(flightId, new JsonObject());

                Map<String, Map<String, Integer>> passengersList = getFlightPassengers(flightId);

                bookingsData.get(flightId).put("date", rs.getTimestamp("date").toString());
                bookingsData.get(flightId).put(DESTINATION_PLANET_PROPERTY_NAME, rs.getString("destinationPlanet"));
                bookingsData.get(flightId).put("bookedSeats", rs.getInt("bookedseats"));
                bookingsData.get(flightId).put("estimatedArrival", rs.getTimestamp("estimatedArrival").toString());
                bookingsData.get(flightId).put("passengers", passengersList);
            }

            return bookingsData;


        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch passengers.", ex);
            throw new RepositoryException("Could not fetch passengers.");
        }
    }

    public void insertBooking(Session session, Booking booking) {
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_BOOKING)
        ) {
            boolean isSingleWay = Boolean.parseBoolean(session.getMetadata().get("isSingleWay"));
            stmt.setString(1, session.getId());
            stmt.setInt(2, isSingleWay ? 1 : 0);
            stmt.setDouble(3, (double) session.getAmountTotal() / 100);
            stmt.setString(4, booking.getFlight().getFlightId());

            stmt.executeUpdate();

            addPassengers(session);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert booking.", ex);
            throw new RepositoryException("Could not insert booking.");
        }
    }

    public void updateBookingPaymentStatus(String bookingId) {
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_BOOKING_STATUS)
        ) {
            stmt.setString(1, ENUM_PAID_BOOKING);
            stmt.setString(2, bookingId);


            stmt.executeUpdate();


        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update booking status.", ex);
            throw new RepositoryException("Could not update booking status.");
        }
    }

    public void addPassenger(Session session, Passenger passenger) {
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_PASSENGER)
        ) {
            stmt.setString(1, session.getId());
            stmt.setString(2, passenger.getUniqueTicketNumber());
            stmt.setString(3, passenger.getMarsId());
            stmt.setString(4, passenger.getSeat());
            stmt.setString(5, session.getMetadata().get("ticketClass"));


            stmt.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert booking.", ex);
            throw new RepositoryException("Could not insert booking.");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql)
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
