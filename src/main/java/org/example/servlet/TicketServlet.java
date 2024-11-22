package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import org.example.dto.TicketDTO;
import org.example.service.TicketService;

import java.io.IOException;
import java.util.List;

@WebServlet("/tickets")
public class TicketServlet extends HttpServlet {

    private final TicketService ticketService = new TicketService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            List<TicketDTO> tickets = ticketService.findAllTickets();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(tickets));
        } catch (Exception e) {
            handleException(response, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            TicketDTO ticketDTO = objectMapper.readValue(request.getReader(), TicketDTO.class);

            TicketDTO savedTicket = ticketService.saveTicket(ticketDTO);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(objectMapper.writeValueAsString(savedTicket));
        } catch (IllegalArgumentException e) {
            handleException(response, e, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            handleException(response, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isBlank()) {
                throw new IllegalArgumentException("Ticket ID is required for deletion");
            }

            Long ticketId = Long.parseLong(idParam);

            boolean isDeleted = ticketService.deleteTicket(ticketId);

            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            handleException(response, e, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            handleException(response, e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleException(HttpServletResponse response, Exception e, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(e.getMessage(), statusCode)
        ));
        e.printStackTrace();
    }

    private static class ErrorResponse {
        private final String message;
        private final int status;

        public ErrorResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }



        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }

}
