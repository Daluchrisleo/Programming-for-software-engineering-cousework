package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Appointment;
import com.boostphysioclinic.model.BookingStatus;
import com.boostphysioclinic.model.Physiotherapist;
import com.boostphysioclinic.model.report.AppointmentReport;
import com.boostphysioclinic.model.report.PhysiotherapistReport;
import com.boostphysioclinic.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for generating various reports based on appointments and physiotherapist data.
 * Uses services to fetch information and produce structured report outputs.
 */
public class ReportGenerator {

    private final PhysiotherapistService physiotherapistService;
    private final AppointmentService appointmentService;

    /**
     * Constructs a {@code ReportGenerator} with the specified services.
     *
     * @param physiotherapistService the service for accessing physiotherapist data
     * @param appointmentService     the service for accessing appointment data
     */
    public ReportGenerator(PhysiotherapistService physiotherapistService, AppointmentService appointmentService) {
        this.physiotherapistService = physiotherapistService;
        this.appointmentService = appointmentService;
    }

    /**
     * Generates a report for all appointments in the system.
     *
     * @return a list of {@code AppointmentReport} representing all appointments
     */
    public List<AppointmentReport> generateAllAppointmentReport() {
        return generateAppointmentReportsForAppointments(appointmentService.getAppointments());
    }

    /**
     * Generates a report for appointments specific to a given physiotherapist.
     *
     * @param physiotherapist the physiotherapist whose appointments to include
     * @return a list of {@code AppointmentReport} for the physiotherapist
     */
    public List<AppointmentReport> generateAppointmentReportForPhysiotherapist(Physiotherapist physiotherapist) {
        List<Appointment> physioAppointments = new ArrayList<>();
        for (Appointment appointment : appointmentService.getAppointments()) {
            if (appointment.getSlot().getPhysiotherapist().equals(physiotherapist)) {
                physioAppointments.add(appointment);
            }
        }
        return generateAppointmentReportsForAppointments(physioAppointments);
    }

    /**
     * Converts a list of appointments into a list of appointment reports.
     *
     * @param appointments the list of appointments to process
     * @return a list of {@code AppointmentReport}
     */
    private List<AppointmentReport> generateAppointmentReportsForAppointments(List<Appointment> appointments) {
        List<AppointmentReport> appointmentReports = new ArrayList<>();
        for (Appointment a : appointments) {
            appointmentReports.add(new AppointmentReport(
                    a.getSlot().getPhysiotherapist().getFullName(),
                    a.getSlot().getTreatment().getName(),
                    a.getPatient().getFullName(),
                    TimeFormatter.formatTime(a.getSlot().getDateTime()),
                    a.getBookingStatus().toString()
            ));
        }
        return appointmentReports;
    }

    /**
     * Generates a summary report for each physiotherapist,
     * showing the number of attended appointments.
     *
     * @return a list of {@code PhysiotherapistReport}
     */
    public List<PhysiotherapistReport> generatePhysiotherapistReport() {
        List<PhysiotherapistReport> physiotherapistReportList = new ArrayList<>();
        physiotherapistService.getAllPhysiotherapists().forEach(physiotherapist -> {
            PhysiotherapistReport r = new PhysiotherapistReport(
                    physiotherapist.getFullName(),
                    getAttendedAppointmentForPhysiotherapist(physiotherapist));
            physiotherapistReportList.add(r);
        });
        return physiotherapistReportList;
    }

    /**
     * Counts the number of attended appointments for a given physiotherapist.
     *
     * @param physiotherapist the physiotherapist whose attended appointments are counted
     * @return the count of attended appointments
     */
    private int getAttendedAppointmentForPhysiotherapist(Physiotherapist physiotherapist) {
        int attendedAppointment = 0;
        for (Appointment appointment : appointmentService.getAppointments()) {
            if (appointment.getBookingStatus() == BookingStatus.Attended &&
                    appointment.getSlot().getPhysiotherapist().equals(physiotherapist)) {
                attendedAppointment++;
            }
        }
        return attendedAppointment;
    }
}
