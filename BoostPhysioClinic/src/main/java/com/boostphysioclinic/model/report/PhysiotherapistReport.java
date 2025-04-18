package com.boostphysioclinic.model.report;

public class PhysiotherapistReport {
    private final String physiotherapistName;
    private final int attendedAppointments;

    public PhysiotherapistReport(String physiotherapistName, int attendedAppointments) {
        this.physiotherapistName = physiotherapistName;
        this.attendedAppointments = attendedAppointments;
    }


    public int getAttendedAppointments() {
        return attendedAppointments;
    }

    public String getPhysiotherapistName() {
        return physiotherapistName;
    }
}
