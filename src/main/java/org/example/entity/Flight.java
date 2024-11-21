package org.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Flight {
    private Long id;
    private String flightNo;
    private LocalDateTime departureDate;
    private String departureAirportCode;
    private LocalDateTime arrivalDate;
    private String arrivalAirportCode;
    private Integer aircraftId;
    private String status;
    private List<Passenger> passengers = new ArrayList<>();

    public Flight(Long id, String flightNo, LocalDateTime departureDate, String departureAirportCode,
                  LocalDateTime arrivalDate, String arrivalAirportCode, Integer aircraftId, String status) {
        this.id = id;
        this.flightNo = flightNo;
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.arrivalDate = arrivalDate;
        this.arrivalAirportCode = arrivalAirportCode;
        this.aircraftId = aircraftId;
        this.status = status;
    }

    public Flight() {
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public Integer getAircraftId() {
        return aircraftId;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public Long getId() {
        return id;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }


    @Override
    public String toString() {
        return "Flight{" +
               "id=" + id +
               ", flightNo='" + flightNo + '\'' +
               ", departureDate=" + departureDate +
               ", departureAirportCode='" + departureAirportCode + '\'' +
               ", arrivalDate=" + arrivalDate +
               ", arrivalAirportCode='" + arrivalAirportCode + '\'' +
               ", aircraftId=" + aircraftId +
               ", status='" + status + '\'' +
               ", passengers=" + passengers +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id) && Objects.equals(flightNo, flight.flightNo) && Objects.equals(departureDate, flight.departureDate) && Objects.equals(departureAirportCode, flight.departureAirportCode) && Objects.equals(arrivalDate, flight.arrivalDate) && Objects.equals(arrivalAirportCode, flight.arrivalAirportCode) && Objects.equals(aircraftId, flight.aircraftId) && Objects.equals(status, flight.status) && Objects.equals(passengers, flight.passengers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightNo, departureDate, departureAirportCode, arrivalDate, arrivalAirportCode, aircraftId, status, passengers);
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.getFlights().add(this);
    }
}
