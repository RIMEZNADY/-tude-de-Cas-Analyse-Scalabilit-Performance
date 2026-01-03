package com.hotel.soap.endpoint.response;

import com.hotel.soap.endpoint.SoapReservation;

public class UpdateReservationResponse {
    private SoapReservation reservation;
    public SoapReservation getReservation() { return reservation; }
    public void setReservation(SoapReservation reservation) { this.reservation = reservation; }
}

