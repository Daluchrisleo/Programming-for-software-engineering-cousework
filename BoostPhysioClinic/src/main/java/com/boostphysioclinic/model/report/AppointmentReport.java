package com.boostphysioclinic.model.report;

/**
 * Represents a report for a specific appointment in the physiotherapy system.
 * Holds summarized information including the physiotherapist, patient, treatment,
 * time of the appointment, and the status of the appointment.
 */
public class AppointmentReport {

    /** Name of the physiotherapist handling the appointment */
    private final String physiotherapistName;

    /** Name of the treatment being administered */
    private final String treatmentName;

    /** Name of the patient receiving treatment */
    private final String patientName;

    /** Time of the appointment in a formatted string */
    private final String time;

    /** Status of the appointment (e.g., Booked, Attended, Missed) */
    private final String appointmentStatus;

    /**
     * Constructs an AppointmentReport with the given details.
     *
     * @param physiotherapistName the name of the physiotherapist
     * @param treatmentName the name of the treatment
     * @param patientName the name of the patient
     * @param time the time of the appointment (formatted)
     * @param appointmentStatus the current status of the appointment
     */
    public AppointmentReport(String physiotherapistName, String treatmentName, String patientName, String time, String appointmentStatus) {
        this.physiotherapistName = physiotherapistName;
        this.treatmentName = treatmentName;
        this.patientName = patientName;
        this.time = time;
        this.appointmentStatus = appointmentStatus;
    }

    /**
     * Gets the name of the physiotherapist.
     *
     * @return the physiotherapist's name
     */
    public String getPhysiotherapistName() {
        return physiotherapistName;
    }

    /**
     * Gets the name of the treatment.
     *
     * @return the treatment name
     */
    public String getTreatmentName() {
        return treatmentName;
    }

    /**
     * Gets the name of the patient.
     *
     * @return the patient's name
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Gets the time of the appointment.
     *
     * @return the formatted appointment time
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the status of the appointment.
     *
     * @return the appointment status
     */
    public String getAppointmentStatus() {
        return appointmentStatus;
    }
}

