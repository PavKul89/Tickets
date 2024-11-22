package org.example.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlightTest {

    private Flight flight1;
    private Flight flight2;
    private Passenger passenger1;
    private Passenger passenger2;

    @BeforeEach
    public void setUp() {

        flight1 = new Flight(101L, "NY123", LocalDateTime.of(2024, 11, 20, 10, 0), "JFK",
                LocalDateTime.of(2024, 11, 20, 13, 0), "LAX", 2001, "On Time");
        flight2 = new Flight(102L, "NY124", LocalDateTime.of(2024, 11, 20, 15, 0), "JFK",
                LocalDateTime.of(2024, 11, 20, 18, 0), "SFO", 2002, "Delayed");


        passenger1 = new Passenger(1L, "John Doe", new ArrayList<>());
        passenger2 = new Passenger(2L, "Jane Smith", new ArrayList<>());
    }


    @Test
    public void testFlightInitialization() {

        assertEquals(101L, flight1.getId(), "Flight ID should be 101");
        assertEquals("NY123", flight1.getFlightNo(), "Flight number should be 'NY123'");
        assertEquals(LocalDateTime.of(2024, 11, 20, 10, 0), flight1.getDepartureDate(), "Departure date should be as expected");
        assertEquals("JFK", flight1.getDepartureAirportCode(), "Departure airport should be 'JFK'");
        assertEquals(LocalDateTime.of(2024, 11, 20, 13, 0), flight1.getArrivalDate(), "Arrival date should be as expected");
        assertEquals("LAX", flight1.getArrivalAirportCode(), "Arrival airport should be 'LAX'");
        assertEquals(2001, flight1.getAircraftId(), "Aircraft ID should be 2001");
        assertEquals("On Time", flight1.getStatus(), "Status should be 'On Time'");
    }

    @Test
    public void testAddPassengerToFlight() {

        flight1.addPassenger(passenger1);

        assertTrue(flight1.getPassengers().contains(passenger1), "Flight should contain passenger1");
        assertTrue(passenger1.getFlights().contains(flight1), "Passenger should have flight1 in their flights list");
    }

    @Test
    public void testAddMultiplePassengersToFlight() {

        flight1.addPassenger(passenger1);
        flight1.addPassenger(passenger2);

        assertTrue(flight1.getPassengers().contains(passenger1), "Flight should contain passenger1");
        assertTrue(flight1.getPassengers().contains(passenger2), "Flight should contain passenger2");

        assertTrue(passenger1.getFlights().contains(flight1), "Passenger1 should have flight1 in their flights list");
        assertTrue(passenger2.getFlights().contains(flight1), "Passenger2 should have flight1 in their flights list");
    }

    @Test
    public void testAddPassengerToMultipleFlights() {

        flight1.addPassenger(passenger1);
        flight2.addPassenger(passenger1);

        assertTrue(flight1.getPassengers().contains(passenger1), "Flight1 should contain passenger1");
        assertTrue(flight2.getPassengers().contains(passenger1), "Flight2 should contain passenger1");

        assertTrue(passenger1.getFlights().contains(flight1), "Passenger1 should have flight1");
        assertTrue(passenger1.getFlights().contains(flight2), "Passenger1 should have flight2");
    }


    @Test
    public void testRemovePassengerFromFlight() {
        flight1.addPassenger(passenger1);
        flight1.getPassengers().remove(passenger1);
        assertFalse(flight1.getPassengers().contains(passenger1));
    }

    @Test
    public void testAddPassengerToFlightWithNullPassenger() {
        assertThrows(NullPointerException.class, () -> flight1.addPassenger(null));
    }


}

