package com.hotel.soap.endpoint;

import com.hotel.soap.endpoint.request.*;
import com.hotel.soap.endpoint.response.*;
import com.hotel.soap.model.Reservation;
import com.hotel.soap.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ReservationEndpoint {
    private static final String NAMESPACE_URI = "http://hotel.com/soap";

    @Autowired
    private ReservationService reservationService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getReservationRequest")
    @ResponsePayload
    public GetReservationResponse getReservation(@RequestPayload GetReservationRequest request) {
        GetReservationResponse response = new GetReservationResponse();
        Reservation reservation = reservationService.getReservation(request.getId());
        response.setReservation(convertToSoapReservation(reservation));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createReservationRequest")
    @ResponsePayload
    public CreateReservationResponse createReservation(@RequestPayload CreateReservationRequest request) {
        CreateReservationResponse response = new CreateReservationResponse();
        Reservation reservation = reservationService.createReservation(
            request.getClientId(),
            request.getChambreId(),
            request.getDateDebut(),
            request.getDateFin(),
            request.getPreferences()
        );
        response.setReservation(convertToSoapReservation(reservation));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateReservationRequest")
    @ResponsePayload
    public UpdateReservationResponse updateReservation(@RequestPayload UpdateReservationRequest request) {
        UpdateReservationResponse response = new UpdateReservationResponse();
        Reservation reservation = reservationService.updateReservation(
            request.getId(),
            request.getClientId(),
            request.getChambreId(),
            request.getDateDebut(),
            request.getDateFin(),
            request.getPreferences()
        );
        response.setReservation(convertToSoapReservation(reservation));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteReservationRequest")
    @ResponsePayload
    public DeleteReservationResponse deleteReservation(@RequestPayload DeleteReservationRequest request) {
        DeleteReservationResponse response = new DeleteReservationResponse();
        reservationService.deleteReservation(request.getId());
        response.setSuccess(true);
        return response;
    }

    private SoapReservation convertToSoapReservation(Reservation reservation) {
        SoapReservation soapReservation = new SoapReservation();
        soapReservation.setId(reservation.getId());
        soapReservation.setClientId(reservation.getClient().getId());
        soapReservation.setChambreId(reservation.getChambre().getId());
        soapReservation.setDateDebut(reservation.getDateDebut().toString());
        soapReservation.setDateFin(reservation.getDateFin().toString());
        soapReservation.setPreferences(reservation.getPreferences());
        return soapReservation;
    }
}

