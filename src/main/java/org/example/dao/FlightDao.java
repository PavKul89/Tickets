package org.example.dao;

import org.example.entity.Flight;
import org.example.entity.Ticket;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight> {

    private static final FlightDao INSTANCE = new FlightDao();

    private static final String FIND_BY_ID_SQL = """
            SELECT id, 
                   flight_no, 
                   departure_date, 
                   departure_airport_code, 
                   arrival_date, 
                   arrival_airport_code, 
                   aircraft_id, 
                   status
            FROM flight
            WHERE id = ?
            """;

    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM flight
            WHERE id = ?
            """;

    private static final String SAVE_FLIGHT_SQL = """
            INSERT INTO flight (flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_FLIGHT_SQL = """
            UPDATE flight
            SET flight_no = ?, 
                departure_date = ?, 
                departure_airport_code = ?, 
                arrival_date = ?, 
                arrival_airport_code = ?, 
                aircraft_id = ?, 
                status = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_FLIGHTS_SQL = """
            SELECT id, 
                   flight_no, 
                   departure_date, 
                   departure_airport_code, 
                   arrival_date, 
                   arrival_airport_code, 
                   aircraft_id, 
                   status
            FROM flight
            """;

    private FlightDao() {
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting flight with id " + id, e);
        }
    }

    @Override
    public Flight save(Flight flight) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_FLIGHT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, flight.getFlightNo());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
            preparedStatement.setString(3, flight.getDepartureAirportCode());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalDate()));
            preparedStatement.setString(5, flight.getArrivalAirportCode());
            preparedStatement.setInt(6, flight.getAircraftId());
            preparedStatement.setString(7, flight.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        flight.setId(generatedKeys.getLong(1));
                    }
                }
            }
            return flight;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving flight", e);
        }
    }

    @Override
    public void update(Flight flight) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_FLIGHT_SQL)) {
            preparedStatement.setString(1, flight.getFlightNo());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
            preparedStatement.setString(3, flight.getDepartureAirportCode());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalDate()));
            preparedStatement.setString(5, flight.getArrivalAirportCode());
            preparedStatement.setInt(6, flight.getAircraftId());
            preparedStatement.setString(7, flight.getStatus());
            preparedStatement.setLong(8, flight.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating flight", e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Flight> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Flight flight = null;

            if (resultSet.next()) {
                flight = new Flight(
                        resultSet.getLong("id"),
                        resultSet.getString("flight_no"),
                        resultSet.getTimestamp("departure_date").toLocalDateTime(),
                        resultSet.getString("departure_airport_code"),
                        resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                        resultSet.getString("arrival_airport_code"),
                        resultSet.getInt("aircraft_id"),
                        resultSet.getString("status")
                );
            }
            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Flight> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_FLIGHTS_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Flight> flights = new ArrayList<>();
            while (resultSet.next()) {
                Flight flight = new Flight(
                        resultSet.getLong("id"),
                        resultSet.getString("flight_no"),
                        resultSet.getTimestamp("departure_date").toLocalDateTime(),
                        resultSet.getString("departure_airport_code"),
                        resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                        resultSet.getString("arrival_airport_code"),
                        resultSet.getInt("aircraft_id"),
                        resultSet.getString("status")
                );
                flights.add(flight);
            }
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all flights", e);
        }
    }
}
