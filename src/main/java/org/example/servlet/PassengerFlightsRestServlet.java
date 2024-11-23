package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.FlightDTO;
import org.example.service.PassengerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/passengerFlights")
public class PassengerFlightsRestServlet extends HttpServlet {

    private final PassengerService passengerService = new PassengerService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String passengerIdStr = request.getParameter("passengerId");

        if (passengerIdStr != null) {
            try {
                Long passengerId = Long.valueOf(passengerIdStr);


                List<FlightDTO> flights = passengerService.getFlightsByPassengerId(passengerId);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");


                String jsonResponse = objectMapper.writeValueAsString(flights);
                response.getWriter().write(jsonResponse);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid passenger ID format");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Passenger ID is required");
        }
    }
}
