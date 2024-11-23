package org.example.dao;

import org.example.dto.TicketFilter;
import org.example.entity.Flight;
import org.example.entity.Ticket;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class TicketDao implements Dao<Long, Ticket> {

    private static final TicketDao INSTANCE = new TicketDao();

    private final FlightDao flightDao = FlightDao.getInstance();

    private TicketDao() {
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }


    private static final String DELETE_SQL = """
            DELETE FROM ticket
            WHERE id = ?
 """;

    private static final String SAVE_SQL = """
            INSERT INTO  ticket(passenger_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?, ?, ?, ?, ?)
""";

    private static final String UPDATE_SQL = """
            UPDATE ticket
            SET
            passenger_no = ?,
            passenger_name = ?,
            flight_id = ?,
            seat_no = ?,
            cost = ? 
            WHERE id =?
            
            """;

    public static final String FIND_BY_ALL = """
        SELECT ticket.id,
            passenger_no,
            passenger_name,
            flight_id,
            seat_no,
            cost,
            f.flight_no,             
            f.status,
            f.aircraft_id,
            f.arrival_airport_code,
            f.arrival_date,
            f.departure_airport_code,
            f.departure_date
        FROM ticket
        JOIN flight f
        ON ticket.flight_id = f.id
        """;


    public static final String FIND_BY_ID  = FIND_BY_ALL + """
            WHERE ticket.id = ?
            """;

    public Ticket save(Ticket ticket) {
        if (ticket.getFlight() == null) {
            throw new IllegalArgumentException("Flight must not be null");
        }

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {

            String checkExistenceSQL = "SELECT COUNT(*) FROM ticket WHERE flight_id = ? AND seat_no = ?";
            try (var checkStatement = connection.prepareStatement(checkExistenceSQL)) {
                checkStatement.setLong(1, ticket.getFlight().getId());
                checkStatement.setString(2, ticket.getSeatNo());
                var resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return ticket;
                }
            }

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().getId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setId(generatedKeys.getLong(1));
                }
            }

            return ticket;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();

             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlight().getId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            Ticket ticket = null;

            if(resultSet.next()) {
                ticket = buildTicket(resultSet);
            }

            return Optional.ofNullable(ticket);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Ticket buildTicket(ResultSet resultSet) throws SQLException {
        var flight = new Flight(
                resultSet.getLong("flight_id"),
                resultSet.getString("flight_no"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                resultSet.getString("status")

        );
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                flightDao.findById(resultSet.getLong("flight_id"),
                        resultSet.getStatement().getConnection()).orElse(null),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public List<Ticket> findAll() {
        try (var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(FIND_BY_ALL)) {
            var resultSet = preparedStatement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));

            }
            return tickets;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ticket> findAll(TicketFilter filter){
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if(filter.seatNo() !=null){
            whereSql.add("seat_no LIKE ?");
            parameters.add("%" + filter.seatNo() + "%");
        }
        if(filter.passengerName() !=null){
            whereSql.add("passenger_name = ?");
            parameters.add(filter.passengerName());
        }

        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var where = whereSql.stream()
                .collect(joining( " AND " ," WHERE ", " LIMIT ? OFFSET ? " ));

        var sql = FIND_BY_ALL + where;

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(preparedStatement);
            var resultSet = preparedStatement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
