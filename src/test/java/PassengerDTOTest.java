package org.example.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassengerDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "John Doe";
        List<FlightDTO> flights = new ArrayList<>();
        flights.add(new FlightDTO(101L, "AA123", LocalDateTime.now(), "New York", LocalDateTime.now().plusHours(2), "Los Angeles", "Economy"));

        // Act
        PassengerDTO passenger = new PassengerDTO(id, name, flights);

        // Assert
        assertEquals(id, passenger.getId());
        assertEquals(name, passenger.getName());
        assertEquals(flights, passenger.getFlights());
    }

    @Test
    void testSetId() {
        // Arrange
        PassengerDTO passenger = new PassengerDTO(null, null, null);
        Long newId = 2L;

        // Act
        passenger.setId(newId);

        // Assert
        assertEquals(newId, passenger.getId());
    }

    @Test
    void testSetName() {
        // Arrange
        PassengerDTO passenger = new PassengerDTO(null, null, null);
        String newName = "Jane Smith";

        // Act
        passenger.setName(newName);

        // Assert
        assertEquals(newName, passenger.getName());
    }

    @Test
    void testSetFlights() {
        // Arrange
        PassengerDTO passenger = new PassengerDTO(null, null, null);
        List<FlightDTO> newFlights = new ArrayList<>();
        newFlights.add(new FlightDTO(102L, "BA456", LocalDateTime.now(), "London", LocalDateTime.now().plusHours(3), "Paris", "Business"));

        // Act
        passenger.setFlights(newFlights);

        // Assert
        assertEquals(newFlights, passenger.getFlights());
    }

    @Test
    void testDefaultValues() {
        // Arrange
        PassengerDTO passenger = new PassengerDTO(null, null, null);

        // Assert
        assertNull(passenger.getId());
        assertNull(passenger.getName());
        assertNull(passenger.getFlights());
    }
}
