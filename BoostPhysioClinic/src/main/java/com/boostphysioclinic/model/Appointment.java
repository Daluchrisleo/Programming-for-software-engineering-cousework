package com.boostphysioclinic.model;

/**
 * Represents an appointment for a patient at a specific timetable slot.
 */
public class Appointment {
    private int appointmentId;
    private BookingStatus bookingStatus;
    private Patient patient;
    private TimetableSlot slot; // 2564396

    /**
     * Creates a new appointment for a patient at a given timetable slot.
     *
     * @param id      the unique appointment ID
     * @param patient the patient for whom the appointment is booked
     * @param slot    the timetable slot for the appointment
     */
    public Appointment(int id, Patient patient, TimetableSlot slot) {
        this.appointmentId = id;
        this.patient = patient;
        this.slot = slot;
        this.bookingStatus = BookingStatus.Booked;
        this.slot.setBooked(true);
    }

    /**
     * Gets the appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Gets the current booking status of the appointment.
     *
     * @return the {@link BookingStatus} of the appointment
     */
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    /**
     * Sets the booking status of the appointment.
     *
     * @param status the new booking status
     */
    public void setBookingStatus(BookingStatus status) {
        this.bookingStatus = status;
    }

    /**
     * Gets the patient associated with this appointment.
     *
     * @return the {@link Patient} object
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Gets the timetable slot assigned to this appointment.
     *
     * @return the {@link TimetableSlot} object
     */
    public TimetableSlot getSlot() {
        return slot;
    }
}
