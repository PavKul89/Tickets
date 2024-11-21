package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Passenger {
    private Long id;
    private String name;
    private List<Flight> flights = new ArrayList<>();

    public Passenger(Long id, String name, List<Flight> flights) {
        this.id = id;
        this.name = name;
        this.flights = flights;
    }

    public Long getId() {
        return id;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.getPassengers().add(this);
    }
}



