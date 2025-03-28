package com.boostphysioclinic.util;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates thread-safe, incrementing 5-digit numeric IDs for patients and appointments.
 * <p>
 * IDs start at 10000 and increment by 1 for each call. The counters are independent for patients
 * and appointments. Note that IDs will eventually exceed 5 digits (e.g., 100000) after 90,000
 * generations.
 * </p>
 */
public class IdGenerator {
    private static final AtomicInteger patientIdCounter = new AtomicInteger(9999);
    private static final AtomicInteger appointmentIdCounter = new AtomicInteger(9999);

    /**
     * Generates a new patient ID.
     *
     * @return the next 5-digit patient ID (starting at 10000)
     */
    public static int generatePatientId() {
        return patientIdCounter.incrementAndGet();
    }

    /**
     * Generates a new appointment ID.
     *
     * @return the next 5-digit appointment ID (starting at 10000)
     */
    public static int generateAppointmentId() {
        return appointmentIdCounter.incrementAndGet();
    }
}