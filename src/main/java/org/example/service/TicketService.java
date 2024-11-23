package org.example.service;

import org.example.dao.FlightDao;
import org.example.dao.TicketDao;
import org.example.dto.TicketDTO;
import org.example.entity.Ticket;
import org.example.entity.Flight;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketService {

    private final TicketDao ticketDao = TicketDao.getInstance();
    private final FlightDao flightDao = FlightDao.getInstance();

    public TicketDTO saveTicket(TicketDTO ticketDTO) {

        Ticket ticket = new Ticket();
        ticket.setPassengerNo(ticketDTO.getPassengerNo());
        ticket.setPassengerName(ticketDTO.getPassengerName());
        ticket.setSeatNo(ticketDTO.getSeatNo());
        ticket.setCost(ticketDTO.getCost());


        Flight flight = flightDao.findById(ticketDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        ticket.setFlight(flight);

        Ticket savedTicket = ticketDao.save(ticket);

        return toDTO(savedTicket);
    }

    public boolean deleteTicket(Long id) {
        return ticketDao.delete(id);
    }


    public TicketDTO updateTicket(TicketDTO ticketDTO) {

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setPassengerNo(ticketDTO.getPassengerNo());
        ticket.setPassengerName(ticketDTO.getPassengerName());
        ticket.setSeatNo(ticketDTO.getSeatNo());
        ticket.setCost(ticketDTO.getCost());

        Flight flight = flightDao.findById(ticketDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        ticket.setFlight(flight);

        ticketDao.update(ticket);

        return toDTO(ticket);
    }


    public Optional<TicketDTO> findTicketById(Long id) {
        Optional<Ticket> ticket = ticketDao.findById(id);
        return ticket.map(this::toDTO);
    }


    public List<TicketDTO> findAllTickets() {
        List<Ticket> tickets = ticketDao.findAll();
        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private TicketDTO toDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPassengerNo(ticket.getPassengerNo());
        ticketDTO.setPassengerName(ticket.getPassengerName());
        ticketDTO.setSeatNo(ticket.getSeatNo());
        ticketDTO.setCost(ticket.getCost());
        ticketDTO.setFlightId(ticket.getFlight().getId());
        return ticketDTO;
    }
}

