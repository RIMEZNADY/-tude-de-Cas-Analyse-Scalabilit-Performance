package com.hotel.soap.service;

import com.hotel.soap.model.*;
import com.hotel.soap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ChambreRepository chambreRepository;

    public Reservation createReservation(Long clientId, Long chambreId, String dateDebut, String dateFin, String preferences) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        Chambre chambre = chambreRepository.findById(chambreId).orElseThrow(() -> new RuntimeException("Chambre not found"));
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(LocalDate.parse(dateDebut));
        reservation.setDateFin(LocalDate.parse(dateFin));
        reservation.setPreferences(preferences);
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public Reservation updateReservation(Long id, Long clientId, Long chambreId, String dateDebut, String dateFin, String preferences) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
        if (clientId != null) reservation.setClient(clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found")));
        if (chambreId != null) reservation.setChambre(chambreRepository.findById(chambreId).orElseThrow(() -> new RuntimeException("Chambre not found")));
        if (dateDebut != null) reservation.setDateDebut(LocalDate.parse(dateDebut));
        if (dateFin != null) reservation.setDateFin(LocalDate.parse(dateFin));
        if (preferences != null) reservation.setPreferences(preferences);
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

