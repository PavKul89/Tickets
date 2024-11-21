package org.example.service;

import org.example.dao.FlightDao;
import org.example.entity.Flight;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    @Test
    void findById_FlightExists_ReturnsFlight() {

        FlightDao flightDaoMock = mock(FlightDao.class);

        Flight expectedFlight = new Flight(
                1L,
                "FL123",
                LocalDateTime.of(2023, 11, 15, 10, 0),
                "JFK",
                LocalDateTime.of(2023, 11, 15, 14, 0),
                "LAX",
                1001,
                "Scheduled"
        );

        when(flightDaoMock.findById(1L)).thenReturn(Optional.of(expectedFlight));
        FlightService flightService = new FlightService(flightDaoMock);

        Flight result = flightService.findById(1L);

        System.out.println("Expected: " + expectedFlight);
        System.out.println("Actual: " + result);

        assertEquals(expectedFlight, result);
    }
}

