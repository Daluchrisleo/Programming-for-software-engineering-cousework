package com.boostphysioclinic.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class IdGeneratorTest {

    @BeforeEach
    void resetCounters() throws Exception {
        // Reset patient counter to 9999 using reflection
        Field patientCounterField = IdGenerator.class.getDeclaredField("personnelIdCounter");
        patientCounterField.setAccessible(true);
        AtomicInteger patientCounter = (AtomicInteger) patientCounterField.get(null);
        patientCounter.set(9999);

        // Reset appointment counter to 9999
        Field appointmentCounterField = IdGenerator.class.getDeclaredField("appointmentIdCounter");
        appointmentCounterField.setAccessible(true);
        AtomicInteger appointmentCounter = (AtomicInteger) appointmentCounterField.get(null);
        appointmentCounter.set(9999);
    }

    @Test
    void testGeneratePersonnelIdStartsAt10000() {
        assertEquals(10000, IdGenerator.generatePersonnelId());
    }

    @Test
    void testGenerateAppointmentIdStartsAt10000() {
        assertEquals(10000, IdGenerator.generateAppointmentId());
    }

    @Test
    void testPatientIdsIncrementCorrectly() {
        assertEquals(10000, IdGenerator.generatePersonnelId());
        assertEquals(10001, IdGenerator.generatePersonnelId());
        assertEquals(10002, IdGenerator.generatePersonnelId());
    }

    @Test
    void testAppointmentIdsIncrementCorrectly() {
        assertEquals(10000, IdGenerator.generateAppointmentId());
        assertEquals(10001, IdGenerator.generateAppointmentId());
        assertEquals(10002, IdGenerator.generateAppointmentId());
    }

    @Test
    void testPatientAndAppointmentIdsAreIndependent() {
        assertEquals(10000, IdGenerator.generatePersonnelId());
        assertEquals(10000, IdGenerator.generateAppointmentId());
        assertEquals(10001, IdGenerator.generatePersonnelId());
        assertEquals(10001, IdGenerator.generateAppointmentId());
    }

    @Test
    void testGeneratedIdsAreUnique() {
        Set<Integer> patientIds = new HashSet<>();
        Set<Integer> appointmentIds = new HashSet<>();

        // Generate 1000 IDs for each type and check uniqueness
        for (int i = 0; i < 1000; i++) {
            assertTrue(patientIds.add(IdGenerator.generatePersonnelId()));
            assertTrue(appointmentIds.add(IdGenerator.generateAppointmentId()));
        }
    }

    @Test
    void testIdsAreAtLeastFiveDigits() {
        assertTrue(IdGenerator.generatePersonnelId() >= 10000);
        assertTrue(IdGenerator.generateAppointmentId() >= 10000);
    }
}