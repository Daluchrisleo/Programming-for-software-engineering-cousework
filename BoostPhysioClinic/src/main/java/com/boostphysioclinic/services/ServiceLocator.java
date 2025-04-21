package com.boostphysioclinic.services;

/**
 * The {@code ServiceLocator} class provides a centralized way to retrieve singleton instances
 * of core service classes such as {@code PatientService}, {@code AppointmentService}, and {@code PhysiotherapistService}.
 *
 * This pattern ensures services are easily initialized and shared across the application.
 */
public class ServiceLocator {
    private static PatientService patientService;
    private static AppointmentService appointmentService;
    private static PhysiotherapistService physiotherapistService;

    /**
     * Returns a singleton instance of {@code PatientService}.
     * Initializes it on first access if not already created.
     *
     * @return the shared {@code PatientService} instance
     */
    public static PatientService getPatientService() {
        if (patientService == null) {
            patientService = new PatientService(); // initialize here once
        }
        return patientService;
    }

    /**
     * Returns a singleton instance of {@code AppointmentService}.
     * Initializes it on first access if not already created.
     *
     * @return the shared {@code AppointmentService} instance
     */
    public static AppointmentService getAppointmentService() {
        if (appointmentService == null) {
            appointmentService = new AppointmentService();
        }
        return appointmentService;
    }

    /**
     * Returns a singleton instance of {@code PhysiotherapistService}.
     * Initializes it on first access if not already created.
     *
     * @return the shared {@code PhysiotherapistService} instance
     */
    public static PhysiotherapistService getPhysiotherapistService() {
        if (physiotherapistService == null) {
            physiotherapistService = new PhysiotherapistService();
        }
        return physiotherapistService;
    }
}
