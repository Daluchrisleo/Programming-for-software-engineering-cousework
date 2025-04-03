package com.boostphysioclinic.model;

import java.time.LocalDateTime;

public class TimetableSlot {
    private Physiotherapist physiotherapist;
    private Treatment treatment;
    private LocalDateTime dateTime;
    private boolean isBooked;

    public TimetableSlot(Physiotherapist physio, Treatment treatment, LocalDateTime dateTime) {
        this.physiotherapist = physio;
        this.treatment = treatment;
        this.dateTime = dateTime;
        this.isBooked = false;
    }

    // Getters and Setters
    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "\nTimetableSlot{" +
                "treatment=" + treatment +
                ", dateTime=" + dateTime +
                ", isBooked=" + isBooked +
                '}';
    }
}