import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.dao.FlightDao;
import org.example.dao.TicketDao;
import org.example.entity.Flight;
import org.example.entity.Ticket;
import org.example.service.TicketService;
import org.example.dto.TicketDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TicketServiceTest {

    @Mock
    private FlightDao mockFlightDao;

    @Mock
    private TicketDao mockTicketDao;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTicket() {

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setPassengerNo("123");
        ticketDTO.setPassengerName("John Doe");
        ticketDTO.setSeatNo("10A");
        ticketDTO.setCost(BigDecimal.valueOf(200.00));
        ticketDTO.setFlightId(1L);

        Flight flight = new Flight(1L, "AA123", LocalDateTime.now(), "New York",
                LocalDateTime.now().plusHours(2), "Los Angeles", 150, "Economy");


        when(mockFlightDao.findById(1L)).thenReturn(Optional.of(flight));

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setPassengerNo("123");
        savedTicket.setPassengerName("John Doe");
        savedTicket.setSeatNo("10A");
        savedTicket.setCost(BigDecimal.valueOf(200.00));
        savedTicket.setFlight(flight);

        when(mockTicketDao.save(any(Ticket.class))).thenReturn(savedTicket);

        TicketDTO savedTicketDTO = ticketService.saveTicket(ticketDTO);

        assertNotNull(savedTicketDTO);
        assertEquals("123", savedTicketDTO.getPassengerNo());
        assertEquals("John Doe", savedTicketDTO.getPassengerName());
        assertEquals("10A", savedTicketDTO.getSeatNo());
        assertEquals(BigDecimal.valueOf(200.00), savedTicketDTO.getCost());
        assertEquals(1L, savedTicketDTO.getFlightId());
    }

    @Test
    void testDeleteTicketNotFound() {

        when(mockTicketDao.delete(1L)).thenReturn(false);
        boolean result = ticketService.deleteTicket(1L);
        assertFalse(result);
    }

    @Test
    void testUpdateTicket() {

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(1L);
        ticketDTO.setPassengerNo("123");
        ticketDTO.setPassengerName("John Doe");
        ticketDTO.setSeatNo("10A");
        ticketDTO.setCost(BigDecimal.valueOf(200.00));
        ticketDTO.setFlightId(1L);


        Flight flight = new Flight(1L, "AA123", LocalDateTime.now(), "New York",
                LocalDateTime.now().plusHours(2), "Los Angeles", 150, "Economy");


        when(mockFlightDao.findById(1L)).thenReturn(Optional.of(flight));

        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(1L);
        updatedTicket.setPassengerNo("123");
        updatedTicket.setPassengerName("John Doe");
        updatedTicket.setSeatNo("10A");
        updatedTicket.setCost(BigDecimal.valueOf(200.00));
        updatedTicket.setFlight(flight);

        doNothing().when(mockTicketDao).update(any(Ticket.class));

        TicketDTO updatedTicketDTO = ticketService.updateTicket(ticketDTO);


        assertNotNull(updatedTicketDTO);
        assertEquals("123", updatedTicketDTO.getPassengerNo());
        assertEquals("John Doe", updatedTicketDTO.getPassengerName());
        assertEquals("10A", updatedTicketDTO.getSeatNo());
        assertEquals(BigDecimal.valueOf(200.00), updatedTicketDTO.getCost());
        assertEquals(1L, updatedTicketDTO.getFlightId());
    }

}
