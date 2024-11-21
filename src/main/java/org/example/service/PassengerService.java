package org.example.service;

import org.example.dao.PassengerDao;
import org.example.dto.FlightDTO;

import java.util.List;

public class PassengerService {

    private static final PassengerService INSTANCE = new PassengerService();

    private final PassengerDao passengerDao = PassengerDao.getInstance();

    public PassengerService() {}


    public static PassengerService getInstance() {
        return INSTANCE;
    }

    public void addFlightToPassenger(Long passengerId, Long flightId) {
        passengerDao.savePassengerFlight(passengerId, flightId);
    }

    public List<FlightDTO> getFlightsByPassengerId(Long passengerId) {
        return passengerDao.findFlightsByPassengerId(passengerId); // Теперь возвращаем FlightDTO
    }
}



