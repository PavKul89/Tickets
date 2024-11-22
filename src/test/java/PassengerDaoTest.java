import org.example.dto.FlightDTO;
import org.example.entity.Flight;
import org.example.dao.PassengerDao;
import org.example.util.ConnectionManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class PassengerDaoTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private PassengerDao passengerDao;

    @BeforeAll
    public static void setUpContainer() {

        System.setProperty("DB_URL", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("DB_USER", POSTGRES_CONTAINER.getUsername());
        System.setProperty("DB_PASSWORD", POSTGRES_CONTAINER.getPassword());
        try (Connection connection = ConnectionManager.get()) {
            // Инициализация базы данных для тестов
            connection.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS flight (
                    id SERIAL PRIMARY KEY,
                    flight_no VARCHAR(255),
                    departure_date TIMESTAMP,
                    departure_airport_code VARCHAR(255),
                    arrival_date TIMESTAMP,
                    arrival_airport_code VARCHAR(255),
                    aircraft_id INTEGER,
                    status VARCHAR(255)
                );
                CREATE TABLE IF NOT EXISTS passenger_flight (
                    passenger_id BIGINT,
                    flight_id BIGINT,
                    PRIMARY KEY (passenger_id, flight_id)
                );
            """);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize test database", e);
        }

    }

    @BeforeEach
    public void setUp() {
        passengerDao = PassengerDao.getInstance();
    }

    @Test
    public void testFindFlightsByPassengerId_NoFlightsFound() {
        // Проверяем, что при отсутствии полетов возвращается пустой список
        List<FlightDTO> flights = passengerDao.findFlightsByPassengerId(999L);
        assertNotNull(flights);
        assertTrue(flights.isEmpty());
    }

    @Test
    public void testSavePassengerFlight_FlightDoesNotExist() {
        // Тестируем, что метод выбрасывает исключение, если рейса не существует
        assertThrows(RuntimeException.class, () -> passengerDao.savePassengerFlight(1L, 999L));
    }

    @Test
    public void testSavePassengerFlight_PassengerDoesNotExist() {
        // Тестируем, что метод выбрасывает исключение, если пассажира не существует
        assertThrows(RuntimeException.class, () -> passengerDao.savePassengerFlight(999L, 1L));
    }


    @Test
    public void testSavePassengerFlight_SqlException() {

        assertThrows(RuntimeException.class, () -> passengerDao.savePassengerFlight(1L, 2L));
    }

    @Test
    public void testSavePassengerFlight_NoPassenger() {

        assertThrows(RuntimeException.class, () -> passengerDao.savePassengerFlight(999L, 2L));
    }

}
