package com.boostphysioclinic.services;

public class ServiceLocator {
    private static PatientService patientService;
    private static AppointmentService appointmentService;
    private static PhysiotherapistService physiotherapistService;

    public static PatientService getPatientService() {
        if (patientService == null) {
            patientService = new PatientService(); // initialize here once
        }
        return patientService;
    }

    public static AppointmentService getAppointmentService() {
        if (appointmentService == null) {
            appointmentService = new AppointmentService();
        }
        return appointmentService;
    }


    public static PhysiotherapistService getPhysiotherapistService() {
        if (physiotherapistService == null) {
            physiotherapistService = new PhysiotherapistService();
        }
        return physiotherapistService;
    }
}
