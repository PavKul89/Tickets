package org.example.dao;

import org.example.dto.TicketFilter;
import org.example.entity.Flight;
import org.example.entity.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class TicketDaoTest {

    @Container
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private TicketDao ticketDao;
    private FlightDao flightDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {

        ticketDao = TicketDao.getInstance();
        flightDao = FlightDao.getInstance();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());


        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS flight (" +
                         "id BIGINT PRIMARY KEY, " +
                         "flight_no VARCHAR(50), " +
                         "departure_date TIMESTAMP, " +
                         "departure_airport_code VARCHAR(50), " +
                         "arrival_date TIMESTAMP, " +
                         "arrival_airport_code VARCHAR(50), " +
                         "aircraft_id INT, " +
                         "status VARCHAR(50)" +
                         ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS ticket (" +
                         "id BIGSERIAL PRIMARY KEY, " +
                         "passenger_no VARCHAR(50), " +
                         "passenger_name VARCHAR(100), " +
                         "flight_id BIGINT REFERENCES flight(id), " +
                         "seat_no VARCHAR(10), " +
                         "cost DECIMAL(10, 2)" +
                         ")");

            stmt.execute("INSERT INTO flight (id, flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status) VALUES " +
                         "(1, 'AB123', '2024-11-15 10:00:00', 'JFK', '2024-11-15 14:00:00', 'LAX', 123, 'On Time')");

            stmt.execute("INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost) VALUES " +
                         "('P001', 'John Doe', 1, '1A', 150.00)");
        }
    }

    @Test
    public void testFindById_TicketNotFound() {
        Optional<Ticket> result = ticketDao.findById(999L);
        assertFalse(result.isPresent(), "Expected Optional to be empty");
    }


    @Test
    public void testFindAllWithFilter() {
        TicketFilter filter = new TicketFilter(10, 0, "John Doe", null);
        List<Ticket> tickets = ticketDao.findAll(filter);

        assertFalse(tickets.isEmpty(), "Expected at least one ticket in the list");
        assertEquals("John Doe", tickets.get(0).getPassengerName());
    }
}

