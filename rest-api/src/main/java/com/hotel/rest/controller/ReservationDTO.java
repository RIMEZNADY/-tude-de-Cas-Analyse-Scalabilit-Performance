package com.hotel.rest.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public class ReservationDTO {
    @NotNull
    private Long clientId;

    @NotNull
    private Long chambreId;

    @NotNull
    @FutureOrPresent
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    private String preferences;

    // Constructors
    public ReservationDTO() {}

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getChambreId() {
        return chambreId;
    }

    public void setChambreId(Long chambreId) {
        this.chambreId = chambreId;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}

