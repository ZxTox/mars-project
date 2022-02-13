drop table if exists quotes;
create table quotes
(
    id    int auto_increment,
    quote varchar(255)
);

drop table if exists bookingPassengers;

drop table if exists bookings;
create table bookings
(
    booking_id varchar(100) NOT NULL,
    is_single_way tinyint(1) DEFAULT NULL,
    price double DEFAULT NOT NULL,
    flight varchar(50) NOT NULL,
    status enum('PAID', 'PENDING', 'CANCELLED') default 'PENDING',
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (booking_id)
);

drop table if exists bookingPassengers;
create table bookingPassengers
(
    booking_id varchar(100) DEFAULT NULL,
    ticket_number varchar(50) NOT NULL,
    mars_id varchar(50) DEFAULT NULL,
    seat varchar(10) DEFAULT NULL,
    ticket_class enum('ROYAL','BUSINESS','ECONOMIC') DEFAULT NULL,
    PRIMARY KEY (ticket_number),
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

drop table if exists flights;
create table flights
(
    flight_id varchar(100) not null,
    destination enum('MARS', 'EARTH') not null,
    depart_time datetime not null,
    estimated_arrival datetime not null,
    constraint FLIGHTS_PK
        primary key (flight_id)
)
