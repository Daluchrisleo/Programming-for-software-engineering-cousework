package com.boostphysioclinic.model.report;

/**
 * Represents a report summarizing the number of attended appointments
 * by a specific physiotherapist.
 */
public class PhysiotherapistReport {

    /** The full name of the physiotherapist */
    private final String physiotherapistName;

    /** The number of appointments the physiotherapist has attended */
    private final int attendedAppointments;

    /**
     * Constructs a PhysiotherapistReport with the given physiotherapist name and number of attended appointments.
     *
     * @param physiotherapistName the name of the physiotherapist
     * @param attendedAppointments the number of appointments attended by the physiotherapist
     */
    public PhysiotherapistReport(String physiotherapistName, int attendedAppointments) {
        this.physiotherapistName = physiotherapistName;
        this.attendedAppointments = attendedAppointments;
    }

    /**
     * Gets the number of appointments attended by the physiotherapist.
     *
     * @return the number of attended appointments
     */
    public int getAttendedAppointments() {
        return attendedAppointments;
    }

    /**
     * Gets the full name of the physiotherapist.
     *
     * @return the physiotherapist's name
     */
    public String getPhysiotherapistName() {
        return physiotherapistName;
    }
}

