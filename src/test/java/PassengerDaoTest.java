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
    }

    @BeforeEach
    public void setUp() {
        passengerDao = PassengerDao.getInstance();
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
