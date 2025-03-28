package com.boostphysioclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import com.boostphysioclinic.model.*;
import com.boostphysioclinic.services.AppointmentService;
import com.boostphysioclinic.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    private AppointmentService service;
    private Patient testPatient;
    private Physiotherapist testPhysio;
    private TimetableSlot availableSlot;
    private TimetableSlot bookedSlot;

    @BeforeEach
    void setUp() {
        service = new AppointmentService();
        testPatient = new Patient(1, "John Doe", "123 Main St", "555-1234");
        testPhysio = new Physiotherapist(1, "Dr. Smith", "456 Oak St", "555-5678", "Sports Therapy");

        LocalDateTime now = LocalDateTime.now();
        availableSlot = new TimetableSlot(testPhysio, new Treatment("Massage"), now);
        bookedSlot = new TimetableSlot(testPhysio, new Treatment("Therapy"), now.plusHours(1));
        bookedSlot.setBooked(true);
    }

    @Test
    void bookAppointment_success_setsBookingStatusToBooked() {
        Result<Integer, AppointmentService.BookingError> result = service.bookAppointment(testPatient, availableSlot);
        assertTrue(result.isSuccess());

        Appointment appointment = service.getAppointmentById(result.getData()).getData();
        assertEquals(BookingStatus.Booked, appointment.getBookingStatus());
    }

    @Test
    void bookAppointment_success_incrementsAppointmentList() {
        int initialSize = service.getAppointments().size();
        service.bookAppointment(testPatient, availableSlot);
        assertEquals(initialSize + 1, service.getAppointments().size());
    }

    @Test
    void bookAppointment_alreadyBookedSlot_returnsError() {
        Result<Integer, AppointmentService.BookingError> result = service.bookAppointment(testPatient, bookedSlot);
        assertTrue(result.isError());
        assertEquals(AppointmentService.BookingError.TIMETABLE_SLOT_ALREADY_BOOKED, result.getError());
    }

    @Test
    void cancelAppointment_success_freesTimetableSlot() {
        Result<Integer, AppointmentService.BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        assertTrue(bookResult.isSuccess());

        service.cancelAppointment(bookResult.getData());
        assertFalse(availableSlot.isBooked());
    }

    @Test
    void cancelAppointment_alreadyCancelled_returnsError() {
        Result<Integer, AppointmentService.BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        Result<Object, AppointmentService.AppointmentError> cancelResult = service.cancelAppointment(bookResult.getData());
        assertEquals(AppointmentService.AppointmentError.APPOINTMENT_CANCELLED, cancelResult.getError());
    }

    @Test
    void attendAppointment_success_setsStatusToAttended() {
        Result<Integer, AppointmentService.BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        Result<Object, AppointmentService.AppointmentError> attendResult = service.attendAppointment(bookResult.getData());

        assertTrue(attendResult.isSuccess());
        Appointment appointment = service.getAppointmentById(bookResult.getData()).getData();
        assertEquals(BookingStatus.Attended, appointment.getBookingStatus());
    }

    @Test
    void attendAppointment_alreadyAttended_returnsError() {
        Result<Integer, AppointmentService.BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.attendAppointment(bookResult.getData());

        Result<Object, AppointmentService.AppointmentError> result = service.attendAppointment(bookResult.getData());
        assertEquals(AppointmentService.AppointmentError.APPOINTMENT_ALREADY_ATTENDED, result.getError());
    }

    @Test
    void bookAppointment_samePatientSameTime_returnsError() {
        // First booking should succeed
        Result<Integer, AppointmentService.BookingError> firstBooking = service.bookAppointment(testPatient, availableSlot);
        assertTrue(firstBooking.isSuccess());

        // Create new slot with same time
        TimetableSlot conflictingSlot = new TimetableSlot(testPhysio, new Treatment("Conflict"), availableSlot.getDateTime());

        // Second booking should fail
        Result<Integer, AppointmentService.BookingError> secondBooking = service.bookAppointment(testPatient, conflictingSlot);
        assertEquals(AppointmentService.BookingError.PATIENT_HAS_EXISTING_APPOINTMENT_FOR_THE_SAME_TIME_SLOT, secondBooking.getError());
    }

    @Test
    void cancelAppointment_nonExistentAppointment_returnsError() {
        Result<Object, AppointmentService.AppointmentError> result = service.cancelAppointment(999);
        assertEquals(AppointmentService.AppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }

    @Test
    void attendAppointment_nonExistentAppointment_returnsError() {
        Result<Object, AppointmentService.AppointmentError> result = service.attendAppointment(999);
        assertEquals(AppointmentService.AppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }

    @Test
    void cancelAppointment_attendedAppointment_returnsError() {
        Result<Integer, AppointmentService.BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.attendAppointment(bookResult.getData());

        Result<Object, AppointmentService.AppointmentError> cancelResult = service.cancelAppointment(bookResult.getData());
        assertEquals(AppointmentService.AppointmentError.CANNOT_CANCEL_ATTENDED_APPOINTMENT, cancelResult.getError());
    }

    @Test
    void bookAppointment_success_addsToPatientAppointments() {
        Result<Integer, AppointmentService.BookingError> result = service.bookAppointment(testPatient, availableSlot);
        assertTrue(result.isSuccess());
        assertTrue(testPatient.getAppointments().contains(result.getData()));
    }

    @Test
    void bookAppointment_failure_doesNotModifyAppointments() {
        int initialSize = service.getAppointments().size();
        service.bookAppointment(testPatient, bookedSlot);
        assertEquals(initialSize, service.getAppointments().size());
    }

    @Test
    void getAppointmentById_notFound_returnsError() {
        Result<Appointment, AppointmentService.AppointmentError> result = service.getAppointmentById(999);
        assertEquals(AppointmentService.AppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }
}