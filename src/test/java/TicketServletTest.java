package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TicketDTO;
import org.example.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TicketServletTest {

    private TicketServlet ticketServlet;
    private TicketService ticketService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);
        ticketServlet = new TicketServlet();
        setField(ticketServlet, "ticketService", ticketService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    void doGet_ReturnsTicketsList() throws Exception {

        TicketDTO mockTicket = new TicketDTO();
        mockTicket.setId(1L);
        mockTicket.setPassengerNo("P12345");
        mockTicket.setPassengerName("John Doe");
        mockTicket.setSeatNo("1A");
        mockTicket.setCost(BigDecimal.valueOf(100.50));
        mockTicket.setFlightId(2L);

        List<TicketDTO> mockTickets = Collections.singletonList(mockTicket);
        when(ticketService.findAllTickets()).thenReturn(mockTickets);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        ticketServlet.doGet(request, response);

        verify(ticketService, times(1)).findAllTickets();
        String jsonResponse = stringWriter.toString();
        List<TicketDTO> resultTickets = objectMapper.readValue(jsonResponse, List.class);
        assertEquals(1, resultTickets.size());
    }

    @Test
    void doDelete_DeletesTicketSuccessfully() throws Exception {

        when(request.getParameter("id")).thenReturn("1");
        when(ticketService.deleteTicket(1L)).thenReturn(true);

        ticketServlet.doDelete(request, response);

        verify(ticketService, times(1)).deleteTicket(1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDelete_TicketNotFound() throws Exception {

        when(request.getParameter("id")).thenReturn("1");
        when(ticketService.deleteTicket(1L)).thenReturn(false);

        ticketServlet.doDelete(request, response);

        verify(ticketService, times(1)).deleteTicket(1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }


    @Test
    void doPost_SavesTicket() throws Exception {

        TicketDTO inputTicket = new TicketDTO();
        inputTicket.setPassengerNo("P12345");
        inputTicket.setPassengerName("Jane Doe");
        inputTicket.setSeatNo("1B");
        inputTicket.setCost(BigDecimal.valueOf(200.75));
        inputTicket.setFlightId(3L);

        TicketDTO savedTicket = new TicketDTO();
        savedTicket.setId(10L);
        savedTicket.setPassengerNo(inputTicket.getPassengerNo());
        savedTicket.setPassengerName(inputTicket.getPassengerName());
        savedTicket.setSeatNo(inputTicket.getSeatNo());
        savedTicket.setCost(inputTicket.getCost());
        savedTicket.setFlightId(inputTicket.getFlightId());

        when(ticketService.saveTicket(any(TicketDTO.class))).thenReturn(savedTicket);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(inputTicket))));

        ticketServlet.doPost(request, response);

        verify(ticketService, times(1)).saveTicket(any(TicketDTO.class));
        String jsonResponse = stringWriter.toString();
        TicketDTO resultTicket = objectMapper.readValue(jsonResponse, TicketDTO.class);

        assertEquals(savedTicket.getId(), resultTicket.getId());
        assertEquals(savedTicket.getPassengerNo(), resultTicket.getPassengerNo());
    }



    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }

}
