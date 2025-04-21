package com.boostphysioclinic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the system.
 * Inherits common personnel fields and behavior from {@link Personnel},
 * and adds functionality to manage patient-specific appointment data.
 */
public class Patient extends Personnel {
    private final List<Integer> appointments = new ArrayList<>();

    /**
     * Constructs a new Patient instance.
     *
     * @param id       the unique ID of the patient
     * @param fullName the full name of the patient
     * @param address  the address of the patient
     * @param tel      the telephone number of the patient
     */
    public Patient(int id, String fullName, String address, String tel) {
        super(id, fullName, address, tel);
    }

    /**
     * Returns the list of appointment IDs associated with the patient.
     *
     * @return a list of appointment IDs
     */
    public List<Integer> getAppointments() {
        return appointments;
    }

    /**
     * Adds an appointment ID to the patient's appointment list.
     *
     * @param appointmentId the ID of the appointment to add
     */
    public void addAppointment(int appointmentId) {
        appointments.add(appointmentId);
    }

    /**
     * Removes an appointment ID from the patient's appointment list.
     *
     * @param appointmentId the ID of the appointment to remove
     */
    public void removeAppointment(int appointmentId) {
        appointments.remove(Integer.valueOf(appointmentId));
    }
}