package com.boostphysioclinic.model;

/**
 * Enum representing the status of an appointment booking.
 */
public enum BookingStatus {
    /**
     * The appointment has been successfully booked.
     */
    Booked,

    /**
     * The appointment has been cancelled.
     */
    Cancelled,

    /**
     * The appointment was attended by the patient.
     */
    Attended
}

