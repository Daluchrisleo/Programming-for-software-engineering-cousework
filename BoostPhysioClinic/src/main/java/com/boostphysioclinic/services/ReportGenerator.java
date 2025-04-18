package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Appointment;
import com.boostphysioclinic.model.BookingStatus;
import com.boostphysioclinic.model.Physiotherapist;
import com.boostphysioclinic.model.report.AppointmentReport;
import com.boostphysioclinic.model.report.PhysiotherapistReport;
import com.boostphysioclinic.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

    private final PhysiotherapistService physiotherapistService;
    private final AppointmentService appointmentService;

    public ReportGenerator(PhysiotherapistService physiotherapistService, AppointmentService appointmentService) {
        this.physiotherapistService = physiotherapistService;
        this.appointmentService = appointmentService;
    }


    public List<AppointmentReport> generateAllAppointmentReport() {
        return generateAppointmentReportsForAppointments(appointmentService.getAppointments());
    }

    public List<AppointmentReport> generateAppointmentReportForPhysiotherapist(Physiotherapist physiotherapist) {
        List<Appointment> physioAppointments = new ArrayList<>();
        for (Appointment appointment : appointmentService.getAppointments()) {
            if (appointment.getSlot().getPhysiotherapist().equals(physiotherapist)) {
                physioAppointments.add(appointment);
            }
        }

        return generateAppointmentReportsForAppointments(physioAppointments);
    }

    private List<AppointmentReport> generateAppointmentReportsForAppointments(List<Appointment> appointment) {
        List<AppointmentReport> appointmentReports = new ArrayList<>();
        for (Appointment a : appointment) {
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

    private int getAttendedAppointmentForPhysiotherapist(Physiotherapist physiotherapist) {
        int attendedAppointment = 0;

        for (Appointment appointment : appointmentService.getAppointments()) {
            if (appointment.getBookingStatus() == BookingStatus.Attended && appointment.getSlot().getPhysiotherapist().equals(physiotherapist)) {
                attendedAppointment++;
            }
        }

        return attendedAppointment;
    }

}
