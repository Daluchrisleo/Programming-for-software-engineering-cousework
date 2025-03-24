package com.boostphysioclinic.model;

public class Appointment {
    private int appointmentId;
    private BookingStatus bookingStatus;
    private Patient patient;
    private TimetableSlot slot;

    public Appointment(int id, Patient patient, TimetableSlot slot) {
        this.appointmentId = id;
        this.patient = patient;
        this.slot = slot;
        this.bookingStatus = BookingStatus.Booked;
        slot.setBooked(true);
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus status) { this.bookingStatus = status; }
    public Patient getPatient() { return patient; }
    public TimetableSlot getSlot() { return slot; }
}