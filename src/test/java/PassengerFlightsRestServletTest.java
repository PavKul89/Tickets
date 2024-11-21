import org.example.servlet.PassengerFlightsRestServlet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class PassengerFlightsRestServletTest {

    private PassengerFlightsRestServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ByteArrayOutputStream byteArrayOutputStream;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws IOException {
        servlet = new PassengerFlightsRestServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        byteArrayOutputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(byteArrayOutputStream);

        doReturn(writer).when(response).getWriter();
    }

    @Test
    public void testDoGet_InvalidPassengerId() throws Exception {
        when(request.getParameter("passengerId")).thenReturn("invalid");
        servlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid passenger ID format");
    }

    @Test
    public void testDoGet_MissingPassengerId() throws Exception {
        when(request.getParameter("passengerId")).thenReturn(null);
        servlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Passenger ID is required");
    }
}
