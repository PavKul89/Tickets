package org.example.service;

import org.example.dao.FlightDao;
import org.example.entity.Flight;

import java.util.Optional;

public class FlightService {

    private final FlightDao flightDao;

    public FlightService() {
        this.flightDao = FlightDao.getInstance();
    }

    public FlightService(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    public Flight findById(Long id) {

        System.out.println("Starting search for flight with ID: " + id);
        Optional<Flight> flightOptional = flightDao.findById(id);

        if (flightOptional.isEmpty()) {
            String errorMessage = "Flight not found with ID: " + id;
            System.err.println(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        Flight flight = flightOptional.get();
        System.out.println("Flight found: " + flight);
        return flight;
    }
}
