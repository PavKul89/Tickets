package org.example.dto;

import java.util.List;

public class PassengerDTO {
    private Long id;
    private String name;
    private List<FlightDTO> flights;

    public PassengerDTO(Long id, String name, List<FlightDTO> flights) {
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

    public List<FlightDTO> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightDTO> flights) {
        this.flights = flights;
    }
}

