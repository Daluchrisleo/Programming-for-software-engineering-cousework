package com.boostphysioclinic.model.report;

public class AppointmentReport {
    private final String physiotherapistName;
    private final String treatmentName;
    private final String patientName;
    private final String time;
    private final String appointmentStatus;

    public AppointmentReport(String physiotherapistName, String treatmentName, String patientName, String time, String appointmentStatus) {
        this.physiotherapistName = physiotherapistName;
        this.treatmentName = treatmentName;
        this.patientName = patientName;
        this.time = time;
        this.appointmentStatus = appointmentStatus;
    }

    public String getPhysiotherapistName() {
        return physiotherapistName;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getTime() {
        return time;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }
}
