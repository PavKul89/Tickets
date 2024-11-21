package org.example.dao;

import org.example.dto.FlightDTO;
import org.example.entity.Flight;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDao {

    private static final PassengerDao INSTANCE = new PassengerDao();

    private PassengerDao() {}

    public static PassengerDao getInstance() {
        return INSTANCE;
    }

    private static final String FIND_FLIGHTS_BY_PASSENGER_ID = """
        SELECT f.id, f.flight_no, f.departure_date, f.departure_airport_code, 
               f.arrival_date, f.arrival_airport_code, f.aircraft_id, f.status
        FROM flight f
        JOIN passenger_flight pf ON f.id = pf.flight_id
        WHERE pf.passenger_id = ?
    """;

    public List<FlightDTO> findFlightsByPassengerId(Long passengerId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_FLIGHTS_BY_PASSENGER_ID)) {
            preparedStatement.setLong(1, passengerId);
            var resultSet = preparedStatement.executeQuery();
            List<FlightDTO> flights = new ArrayList<>();
            while (resultSet.next()) {
                flights.add(new FlightDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("flight_no"),
                        resultSet.getTimestamp("departure_date").toLocalDateTime(),
                        resultSet.getString("departure_airport_code"),
                        resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                        resultSet.getString("arrival_airport_code"),
                        resultSet.getString("status")
                ));
            }
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePassengerFlight(Long passengerId, Long flightId) {
        String sql = "INSERT INTO passenger_flight (passenger_id, flight_id) VALUES (?, ?)";
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, passengerId);
            preparedStatement.setLong(2, flightId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
