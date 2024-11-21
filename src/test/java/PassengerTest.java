package org.example.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PassengerTest {

    private Passenger passenger1;
    private Passenger passenger2;
    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    public void setUp() {

        flight1 = new Flight(101L, "NY123", null, "JFK", null, "LAX", 2001, "On Time");
        flight2 = new Flight(102L, "NY124", null, "JFK", null, "SFO", 2002, "Delayed");

        passenger1 = new Passenger(1L, "John Doe", new ArrayList<>());
        passenger2 = new Passenger(2L, "Jane Smith", new ArrayList<>());
    }

    @Test
    public void testPassengerInitialization() {

        assertEquals(1L, passenger1.getId(), "Passenger ID should be 1");
        assertEquals("John Doe", passenger1.getName(), "Passenger name should be 'John Doe'");
        assertTrue(passenger1.getFlights().isEmpty(), "Passenger flights list should be empty initially");
    }

    @Test
    public void testAddFlightToPassenger() {

        passenger1.addFlight(flight1);

        assertTrue(passenger1.getFlights().contains(flight1), "Passenger should have flight1 in their flight list");

        assertTrue(flight1.getPassengers().contains(passenger1), "Flight should contain passenger1 in its passengers list");
    }

    @Test
    public void testAddMultipleFlightsToPassenger() {

        passenger1.addFlight(flight1);
        passenger1.addFlight(flight2);

        assertTrue(passenger1.getFlights().contains(flight1), "Passenger should have flight1 in their flight list");
        assertTrue(passenger1.getFlights().contains(flight2), "Passenger should have flight2 in their flight list");

        assertTrue(flight1.getPassengers().contains(passenger1), "Flight1 should contain passenger1 in its passengers list");
        assertTrue(flight2.getPassengers().contains(passenger1), "Flight2 should contain passenger1 in its passengers list");
    }


    @Test
    public void testFlightListConsistency() {

        passenger1.addFlight(flight1);
        passenger1.addFlight(flight2);

        assertTrue(passenger1.getFlights().contains(flight1), "Passenger should have flight1 in their flight list");
        assertTrue(passenger1.getFlights().contains(flight2), "Passenger should have flight2 in their flight list");

        assertTrue(flight1.getPassengers().contains(passenger1), "Flight1 should contain passenger1");
        assertTrue(flight2.getPassengers().contains(passenger1), "Flight2 should contain passenger1");
    }

    @Test
    public void testSettersAndGetters() {

        passenger1.setId(3L);
        passenger1.setName("John Wick");

        assertEquals(3L, passenger1.getId(), "Passenger ID should be 3");
        assertEquals("John Wick", passenger1.getName(), "Passenger name should be 'John Wick'");
    }
}
