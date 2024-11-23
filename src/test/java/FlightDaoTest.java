package org.example.dao;

import org.example.entity.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Testcontainers
public class FlightDaoTest {

    @Container
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private FlightDao flightDao;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {

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
            stmt.execute("INSERT INTO flight (id, flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status) VALUES " +
                         "(1, 'AB123', '2024-11-15 10:00:00', 'JFK', '2024-11-15 14:00:00', 'LAX', 123, 'On Time')");
        }
    }

    @Test
    public void testFindById_FlightFound() throws SQLException {
        Optional<Flight> result = flightDao.findById(1L, connection);
        assertTrue(result.isPresent(), "Expected Optional to contain a flight");

        Flight flight = result.get();
        assertEquals(1L, flight.getId());
        assertEquals("AB123", flight.getFlightNo());
        assertEquals("JFK", flight.getDepartureAirportCode());
        assertEquals("LAX", flight.getArrivalAirportCode());
        assertEquals("On Time", flight.getStatus());
    }

    @Test
    public void testFindById_FlightNotFound() throws SQLException {
        Optional<Flight> result = flightDao.findById(999L, connection);
        assertFalse(result.isPresent(), "Expected Optional to be empty");
    }

    @Test
    public void testFindById_SqlException() throws SQLException {

        Connection brokenConnection = Mockito.mock(Connection.class);
        when(brokenConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));
        assertThrows(RuntimeException.class, () -> flightDao.findById(1L, brokenConnection));
    }

    @Test
    public void testFindAll_Flights() throws SQLException {
        List<Flight> flights = flightDao.findAll();
        assertFalse(flights.isEmpty(), "Expected at least one flight in the list");
    }
}
