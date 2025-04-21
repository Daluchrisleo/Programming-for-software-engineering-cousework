package com.boostphysioclinic.model;

import java.time.LocalDateTime;

/**
 * Represents a slot in a timetable for physiotherapy treatment.
 * Contains information about the assigned physiotherapist, treatment,
 * scheduled time, and whether the slot has been booked.
 */
public class TimetableSlot {
    private Physiotherapist physiotherapist;
    private Treatment treatment;
    private LocalDateTime dateTime;
    private boolean isBooked;

    /**
     * Constructs a new {@code TimetableSlot} with the specified physiotherapist,
     * treatment, and date/time. The slot is initially marked as unbooked.
     *
     * @param physio     the physiotherapist assigned to this slot
     * @param treatment  the treatment scheduled in this slot
     * @param dateTime   the date and time of the slot
     */
    public TimetableSlot(Physiotherapist physio, Treatment treatment, LocalDateTime dateTime) {
        this.physiotherapist = physio;
        this.treatment = treatment;
        this.dateTime = dateTime;
        this.isBooked = false;
    }

    /**
     * Returns the physiotherapist assigned to this slot.
     *
     * @return the physiotherapist
     */
    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    /**
     * Returns the treatment scheduled in this slot.
     *
     * @return the treatment
     */
    public Treatment getTreatment() {
        return treatment;
    }

    /**
     * Returns the date and time of this timetable slot.
     *
     * @return the slot's date and time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Indicates whether this slot is booked.
     *
     * @return {@code true} if the slot is booked; {@code false} otherwise
     */
    public boolean isBooked() {
        return isBooked;
    }

    /**
     * Sets the booking status of this slot.
     *
     * @param booked {@code true} to mark the slot as booked, {@code false} to unmark it
     */
    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    /**
     * Returns a string representation of this timetable slot.
     *
     * @return a string describing the treatment, date/time, and booking status
     */
    @Override
    public String toString() {
        return "\nTimetableSlot{" +
                "treatment=" + treatment +
                ", dateTime=" + dateTime +
                ", isBooked=" + isBooked +
                '}';
    }
}