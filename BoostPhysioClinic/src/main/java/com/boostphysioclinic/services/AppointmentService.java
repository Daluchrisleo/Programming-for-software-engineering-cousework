package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Appointment;
import com.boostphysioclinic.model.BookingStatus;
import com.boostphysioclinic.model.Patient;
import com.boostphysioclinic.model.TimetableSlot;
import com.boostphysioclinic.util.IdGenerator;
import com.boostphysioclinic.util.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing appointments.
 * Provides methods to book, retrieve, attend, and cancel appointments.
 */
public class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();

    /**
     * Books an appointment for a patient in the specified time slot.
     *
     * @param patient the patient for whom the booking is being made
     * @param slot    the available timetable slot for the appointment
     * @return a {@link Result} containing the appointment ID if successful,
     * otherwise an error result with the {@link BookingError} reason for failure
     */
    public Result<Integer, BookingError> bookAppointment(Patient patient, TimetableSlot slot) {
        if (slot.isBooked()) {
            return Result.error(BookingError.TIMETABLE_SLOT_ALREADY_BOOKED);
        }

        boolean hasAppointAtSameTimeSlot = patient.getAppointments().stream()
                .map(this::getAppointmentById)
                .filter(Result::isSuccess)
                .map(Result::getData)
                .anyMatch(existingAppointment ->
                        existingAppointment.getSlot().getDateTime().isEqual(slot.getDateTime())
                );

        if (hasAppointAtSameTimeSlot) {
            return Result.error(BookingError.PATIENT_HAS_EXISTING_APPOINTMENT_FOR_THE_SAME_TIME_SLOT);
        }

        var appointmentID = IdGenerator.generateAppointmentId();
        Appointment appointment = new Appointment(appointmentID, patient, slot);
        slot.setBooked(true);
        patient.addAppointment(appointmentID);
        appointments.add(appointment);
        return Result.success(appointmentID);
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param id the appointment ID
     * @return a {@link Result} containing the {@link Appointment} if found,
     * otherwise an error result with {@link AppointmentError#APPOINTMENT_NOT_FOUND}
     */
    public Result<Appointment, AppointmentError> getAppointmentById(int id) {
        Appointment appointment = appointments.stream()
                .filter(a -> a.getAppointmentId() == id)
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            return Result.error(AppointmentError.APPOINTMENT_NOT_FOUND);
        } else {
            return Result.success(appointment);
        }
    }

    /**
     * Marks an appointment as attended.
     *
     * @param appointmentId the ID of the appointment to be attended
     * @return a {@link Result} indicating success or an error with the relevant {@link AppointmentError}
     */
    public Result<Object, AppointmentError> attendAppointment(int appointmentId) {
        var result = getAppointmentById(appointmentId);

        if (result.isError()) {
            return Result.error(AppointmentError.APPOINTMENT_NOT_FOUND);
        }

        Appointment appointment = result.getData();

        if (appointment.getBookingStatus() == BookingStatus.Attended) {
            return Result.error(AppointmentError.APPOINTMENT_ALREADY_ATTENDED);
        }

        if (appointment.getBookingStatus() == BookingStatus.Cancelled) {
            return Result.error(AppointmentError.APPOINTMENT_CANCELLED);
        }

        // Mark appointment as attended
        appointment.setBookingStatus(BookingStatus.Attended);
        return Result.success(Result.NO_VALUE);
    }

    /**
     * Cancels an appointment.
     *
     * @param appointmentId the ID of the appointment to be canceled
     * @return a {@link Result} indicating success or an error with the relevant {@link AppointmentError}
     */
    public Result<Object, AppointmentError> cancelAppointment(int appointmentId) {
        var result = getAppointmentById(appointmentId);

        if (result.isError()) {
            return Result.error(AppointmentError.APPOINTMENT_NOT_FOUND);
        }

        Appointment appointment = result.getData();

        if (appointment.getBookingStatus() == BookingStatus.Cancelled) {
            return Result.error(AppointmentError.APPOINTMENT_CANCELLED);
        }

        if (appointment.getBookingStatus() == BookingStatus.Attended) {
            return Result.error(AppointmentError.CANNOT_CANCEL_ATTENDED_APPOINTMENT);
        }

        // Cancel appointment
        appointment.setBookingStatus(BookingStatus.Cancelled);
        appointment.getSlot().setBooked(false);
        return Result.success(Result.NO_VALUE);
    }

    /**
     * Returns the list of all booked appointments.
     *
     * @return a list of {@link Appointment} objects
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Enum representing possible errors when handling appointments.
     */
    public enum AppointmentError {
        APPOINTMENT_ALREADY_ATTENDED,
        APPOINTMENT_CANCELLED,
        APPOINTMENT_NOT_FOUND,
        CANNOT_CANCEL_ATTENDED_APPOINTMENT
    }

    /**
     * Enum representing possible errors when booking an appointment.
     */
    public enum BookingError {
        TIMETABLE_SLOT_ALREADY_BOOKED,
        PATIENT_HAS_EXISTING_APPOINTMENT_FOR_THE_SAME_TIME_SLOT
    }
}
