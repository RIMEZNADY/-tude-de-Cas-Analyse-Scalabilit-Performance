package com.hotel.rest.service;

import com.hotel.rest.controller.ReservationDTO;
import com.hotel.rest.model.Chambre;
import com.hotel.rest.model.Client;
import com.hotel.rest.model.Reservation;
import com.hotel.rest.repository.ChambreRepository;
import com.hotel.rest.repository.ClientRepository;
import com.hotel.rest.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    public Reservation createReservation(ReservationDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Chambre chambre = chambreRepository.findById(dto.getChambreId())
                .orElseThrow(() -> new RuntimeException("Chambre not found"));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setPreferences(dto.getPreferences());

        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public Reservation updateReservation(Long id, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            reservation.setClient(client);
        }

        if (dto.getChambreId() != null) {
            Chambre chambre = chambreRepository.findById(dto.getChambreId())
                    .orElseThrow(() -> new RuntimeException("Chambre not found"));
            reservation.setChambre(chambre);
        }

        if (dto.getDateDebut() != null) {
            reservation.setDateDebut(dto.getDateDebut());
        }

        if (dto.getDateFin() != null) {
            reservation.setDateFin(dto.getDateFin());
        }

        if (dto.getPreferences() != null) {
            reservation.setPreferences(dto.getPreferences());
        }

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}

