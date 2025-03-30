package com.boostphysioclinic.services;

import static com.boostphysioclinic.services.AppointmentService.*;
import static org.junit.jupiter.api.Assertions.*;

import com.boostphysioclinic.model.*;
import com.boostphysioclinic.services.AppointmentService;
import com.boostphysioclinic.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        testPhysio = new Physiotherapist(1, "Dr. Smith", "456 Oak St", "555-5678", List.of("Sports Therapy"));

        LocalDateTime now = LocalDateTime.now();
        availableSlot = new TimetableSlot(testPhysio, new Treatment("Massage"), now);
        bookedSlot = new TimetableSlot(testPhysio, new Treatment("Therapy"), now.plusHours(1));
        bookedSlot.setBooked(true);
    }

    @Test
    void bookAppointment_success_setsBookingStatusToBooked() {
        Result<Integer, BookingError> result = service.bookAppointment(testPatient, availableSlot);
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
        Result<Integer, BookingError> result = service.bookAppointment(testPatient, bookedSlot);
        assertTrue(result.isError());
        assertEquals(BookingError.TIMETABLE_SLOT_ALREADY_BOOKED, result.getError());
    }

    @Test
    void cancelAppointment_success_freesTimetableSlot() {
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        assertTrue(bookResult.isSuccess());

        service.cancelAppointment(bookResult.getData());
        assertFalse(availableSlot.isBooked());
    }

    @Test
    void cancelAppointment_alreadyCancelled_returnsError() {
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        Result<Object, AppointmentError> cancelResult = service.cancelAppointment(bookResult.getData());
        assertEquals(AppointmentError.APPOINTMENT_CANCELLED, cancelResult.getError());
    }

    @Test
    void attendAppointment_success_setsStatusToAttended() {
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        Result<Object, AppointmentError> attendResult = service.attendAppointment(bookResult.getData());

        assertTrue(attendResult.isSuccess());
        Appointment appointment = service.getAppointmentById(bookResult.getData()).getData();
        assertEquals(BookingStatus.Attended, appointment.getBookingStatus());
    }

    @Test
    void attendAppointment_alreadyAttended_returnsError() {
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.attendAppointment(bookResult.getData());

        Result<Object, AppointmentError> result = service.attendAppointment(bookResult.getData());
        assertEquals(AppointmentError.APPOINTMENT_ALREADY_ATTENDED, result.getError());
    }

    @Test
    void bookAppointment_samePatientSameTime_returnsError() {
        // First booking should succeed
        Result<Integer, BookingError> firstBooking = service.bookAppointment(testPatient, availableSlot);
        assertTrue(firstBooking.isSuccess());

        // Create new slot with same time
        TimetableSlot conflictingSlot = new TimetableSlot(testPhysio, new Treatment("Conflict"), availableSlot.getDateTime());

        // Second booking should fail
        Result<Integer, BookingError> secondBooking = service.bookAppointment(testPatient, conflictingSlot);
        assertEquals(BookingError.PATIENT_HAS_EXISTING_APPOINTMENT_FOR_THE_SAME_TIME_SLOT, secondBooking.getError());
    }

    @Test
    void cancelAppointment_nonExistentAppointment_returnsError() {
        Result<Object, AppointmentError> result = service.cancelAppointment(999);
        assertEquals(AppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }

    @Test
    void attendAppointment_nonExistentAppointment_returnsError() {
        Result<Object, AppointmentError> result = service.attendAppointment(999);
        assertEquals(AppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }

    @Test
    void cancelAppointment_attendedAppointment_returnsError() {
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.attendAppointment(bookResult.getData());

        Result<Object, AppointmentError> cancelResult = service.cancelAppointment(bookResult.getData());
        assertEquals(AppointmentError.CANNOT_CANCEL_ATTENDED_APPOINTMENT, cancelResult.getError());
    }

    @Test
    void bookAppointment_success_addsToPatientAppointments() {
        Result<Integer, BookingError> result = service.bookAppointment(testPatient, availableSlot);
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
        Result<Appointment, AppointmentError> result = service.getAppointmentById(999);
        assertEquals(AppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }

    @Test
    void rebookAppointment_successfulRebooking() {
        // Book and cancel original appointment
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        var result = service.rebookAppointment(bookResult.getData());

        assertTrue(result.isSuccess());
        Appointment appointment = service.getAppointmentById(bookResult.getData()).getData();
        assertEquals(BookingStatus.Booked, appointment.getBookingStatus());
        assertTrue(appointment.getSlot().isBooked());
        assertTrue(testPatient.getAppointments().contains(appointment.getAppointmentId()));
    }

    @Test
    void rebookAppointment_appointmentNotFound() {
        var result = service.rebookAppointment(999);
        assertEquals(RebookAppointmentError.APPOINTMENT_NOT_FOUND, result.getError());
    }

    @Test
    void rebookAppointment_notCancelled() {
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);

        var result = service.rebookAppointment(bookResult.getData());
        assertEquals(RebookAppointmentError.APPOINTMENT_NOT_CANCELLED, result.getError());
    }

    @Test
    void rebookAppointment_slotNoLongerAvailable() {
        // Book, cancel, then occupy the slot
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        // Book slot with different patient
        Patient otherPatient = new Patient(2, "Jane Doe", "456 Oak St", "555-5678");
        service.bookAppointment(otherPatient, availableSlot);

        var result = service.rebookAppointment(bookResult.getData());
        assertEquals(RebookAppointmentError.APPOINTMENT_SLOT_NO_LONGER_AVAILABLE, result.getError());
    }

    @Test
    void rebookAppointment_patientHasConflict() {
        // Book and cancel original
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        // Create new appointment at same time
        TimetableSlot sameTimeSlot = new TimetableSlot(
                testPhysio,
                new Treatment("Conflict"),
                availableSlot.getDateTime()
        );
        service.bookAppointment(testPatient, sameTimeSlot);

        var result = service.rebookAppointment(bookResult.getData());
        assertEquals(RebookAppointmentError.PATIENT_HAS_ANOTHER_APPOINTMENT_AT_SAME_TIME, result.getError());
    }

    @Test
    void rebookAppointment_originalSlotReassigned() {
        // Book and cancel original
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        var result = service.rebookAppointment(bookResult.getData());
        assertTrue(result.isSuccess());
        assertEquals(availableSlot.getDateTime(),
                service.getAppointmentById(bookResult.getData()).getData().getSlot().getDateTime());
    }

    @Test
    void rebookAppointment_alreadyRebooked() {
        // Book, cancel, and rebook once
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());
        service.rebookAppointment(bookResult.getData());

        // Try to rebook again
        var result = service.rebookAppointment(bookResult.getData());
        assertEquals(RebookAppointmentError.APPOINTMENT_NOT_CANCELLED, result.getError());
    }

    @Test
    void rebookAppointment_attendedAppointment() {
        // Book, attend, then try to rebook
        Result<Integer, BookingError> bookResult = service.bookAppointment(testPatient, availableSlot);
        service.attendAppointment(bookResult.getData());

        var result = service.rebookAppointment(bookResult.getData());
        assertEquals(RebookAppointmentError.APPOINTMENT_NOT_CANCELLED, result.getError());
    }

    @Test
    void rebookAppointment_multipleCancelledAppointments() {
        // Create two cancelled appointments
        Result<Integer, BookingError> appt1 = service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(appt1.getData());

        LocalDateTime newTime = LocalDateTime.now().plusHours(2);
        TimetableSlot slot2 = new TimetableSlot(testPhysio, new Treatment("Therapy"), newTime);
        Result<Integer, BookingError> appt2 = service.bookAppointment(testPatient, slot2);
        service.cancelAppointment(appt2.getData());

        // Rebook both
        var result1 = service.rebookAppointment(appt1.getData());
        var result2 = service.rebookAppointment(appt2.getData());

        assertAll(
                () -> assertTrue(result1.isSuccess()),
                () -> assertTrue(result2.isSuccess()),
                () -> assertEquals(2, testPatient.getAppointments().size())
        );
    }

    @Test
    void bookAppointment_afterCancellationAllowsNewBookingAtSameTime() {
        // Book initial appointment
        Result<Integer, AppointmentService.BookingError> bookResult =
                service.bookAppointment(testPatient, availableSlot);
        assertTrue(bookResult.isSuccess());
        int originalAppointmentId = bookResult.getData();

        // Verify initial state
        assertTrue(availableSlot.isBooked());
        assertTrue(testPatient.getAppointments().contains(originalAppointmentId));

        // Cancel the appointment
        Result<Object, AppointmentService.AppointmentError> cancelResult =
                service.cancelAppointment(originalAppointmentId);
        assertTrue(cancelResult.isSuccess());

        // Verify cancellation effects
        assertFalse(availableSlot.isBooked());
        assertEquals(BookingStatus.Cancelled,
                service.getAppointmentById(originalAppointmentId).getData().getBookingStatus());

        // Create new slot with same time but different treatment
        TimetableSlot newSlot = new TimetableSlot(
                testPhysio,
                new Treatment("New Treatment"),
                availableSlot.getDateTime()
        );

        // Book new appointment
        Result<Integer, AppointmentService.BookingError> newBookResult =
                service.bookAppointment(testPatient, newSlot);

        // Verify new booking success
        assertTrue(newBookResult.isSuccess());
        assertTrue(newSlot.isBooked());
        assertTrue(testPatient.getAppointments().contains(newBookResult.getData()));

        // Verify both appointments exist with correct statuses
        assertEquals(BookingStatus.Cancelled,
                service.getAppointmentById(originalAppointmentId).getData().getBookingStatus());
        assertEquals(BookingStatus.Booked,
                service.getAppointmentById(newBookResult.getData()).getData().getBookingStatus());
    }

    @Test
    void bookAppointment_afterCancellationSameSlotReuse() {
        // Book and cancel appointment
        Result<Integer, AppointmentService.BookingError> bookResult =
                service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(bookResult.getData());

        // Re-book the same slot
        Result<Integer, AppointmentService.BookingError> rebookResult =
                service.bookAppointment(testPatient, availableSlot);

        assertTrue(rebookResult.isSuccess());
        assertTrue(availableSlot.isBooked());
        assertEquals(2, testPatient.getAppointments().size());
    }

    @Test
    void bookAppointment_afterCancellationWithMultipleSlotsSameTime() {
        // Create multiple slots at same time
        LocalDateTime slotTime = LocalDateTime.now();
        TimetableSlot slot1 = new TimetableSlot(testPhysio, new Treatment("Massage"), slotTime);
        TimetableSlot slot2 = new TimetableSlot(testPhysio, new Treatment("Therapy"), slotTime);

        // Book and cancel first slot
        Result<Integer, AppointmentService.BookingError> firstBooking =
                service.bookAppointment(testPatient, slot1);
        service.cancelAppointment(firstBooking.getData());

        // Book second slot
        Result<Integer, AppointmentService.BookingError> secondBooking =
                service.bookAppointment(testPatient, slot2);

        assertTrue(secondBooking.isSuccess());
        assertTrue(slot2.isBooked());
        assertEquals(2, testPatient.getAppointments().size());
    }

    @Test
    void bookAppointment_afterCancellationPreventsDoubleBooking() {
        // Book and cancel
        Result<Integer, AppointmentService.BookingError> firstBook =
                service.bookAppointment(testPatient, availableSlot);
        service.cancelAppointment(firstBook.getData());

        // Re-book successfully
        Result<Integer, AppointmentService.BookingError> secondBook =
                service.bookAppointment(testPatient, availableSlot);
        assertTrue(secondBook.isSuccess());

        // Attempt to book again should fail
        Result<Integer, AppointmentService.BookingError> thirdBook =
                service.bookAppointment(testPatient, availableSlot);
        assertEquals(AppointmentService.BookingError.TIMETABLE_SLOT_ALREADY_BOOKED, thirdBook.getError());
    }
}