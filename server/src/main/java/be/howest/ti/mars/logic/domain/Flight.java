package be.howest.ti.mars.logic.domain;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Flight {

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";

    private final String flightId;
    private final String destinationPlanet;
    private final LocalDateTime departTime;
    private final LocalDateTime estimatedArrival;


    public Flight(String flightId, String destinationPlanet, LocalDateTime departTime, LocalDateTime estimatedArrival) {
        this.flightId = flightId;
        this.destinationPlanet = destinationPlanet;
        this.departTime = departTime;
        this.estimatedArrival = estimatedArrival;
    }

    public Flight(String flightId, String destinationPlanet, String departTime, String estimatedArrival) {
        this(flightId, destinationPlanet, convertToTime(departTime), convertToTime(estimatedArrival));
    }



    public Flight(String destinationPlanet, String departTime, String estimatedArrival) {
        this(UUID.randomUUID().toString(), destinationPlanet, departTime, estimatedArrival);
    }

    public long getTravelTime() {
        return ChronoUnit.MONTHS.between(departTime, estimatedArrival);
    }

    public String getDestinationPlanet() {
        return destinationPlanet;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getDepartTime() {
        return departTime.toString();
    }

    public String getEstimatedArrival() {
        return estimatedArrival.toString();
    }

    private static LocalDateTime convertToTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        return LocalDateTime.parse(time, formatter);
    }
}
