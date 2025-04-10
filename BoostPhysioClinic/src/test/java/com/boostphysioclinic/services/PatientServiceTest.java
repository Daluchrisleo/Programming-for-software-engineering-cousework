package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Patient;
import com.boostphysioclinic.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {

    private PatientService SUT;
    private final String validName = "John Doe";
    private final String validNumber = "+1234567";
    private final String validAddress = "123 Main Street";

    @BeforeEach
    void setUp() {
        SUT = new PatientService();
    }

    @Test
    void getPatientsList_onStart_returnsEmptyList() {
        assertTrue(SUT.getPatientsList().isEmpty());
    }

    @Test
    void addPatient_validPatient_incrementsListSize() {
        var result = SUT.addPatient(validName, validAddress, validNumber);
        assertTrue(result.isSuccess());
        assertEquals(1, SUT.getPatientsList().size());
    }

    @Test
    void deletePatient_existingPatient_decrementsListSize() {
        SUT.addPatient(validName, validAddress, validNumber);
        int patientId = SUT.getPatientsList().get(0).getId();
        boolean isDeleted = SUT.deletePatient(patientId);
        assertTrue(isDeleted);
        assertTrue(SUT.getPatientsList().isEmpty());
    }

    @Test
    void addPatient_nameTooShort_returnsError() {
        var result = SUT.addPatient("Jo", "123 Main Street", "555-123-4567");
        assertTrue(result.isError());
    }

    @Test
    void addPatient_nameTooShort_hasCorrectErrorType() {
        var result = SUT.addPatient("Jo", validAddress, validNumber);
        assertEquals(PatientService.Error.NAME_TOO_SHORT, result.getError());
    }

    @Test
    void addPatient_duplicateName_returnsError() {
        SUT.addPatient("John Doe", validAddress, validNumber);
        var result = SUT.addPatient("John Doe", validAddress, validNumber);
        assertTrue(result.isError());
    }

    @Test
    void addPatient_duplicateName_hasCorrectErrorType() {
        SUT.addPatient("John Doe", validAddress, validNumber);
        var result = SUT.addPatient("John Doe", validAddress, validNumber);
        assertEquals(PatientService.Error.PATIENT_EXISTS, result.getError());
    }

    @Test
    void addPatient_invalidTelephone_returnsError() {
        var result = SUT.addPatient(validName, validAddress, "invalid");
        assertTrue(result.isError());
        assertEquals(PatientService.Error.INVALID_TELEPHONE, result.getError());
    }

    @Test
    void addPatient_addressLessThan3Characters_returnsError() {
        var result = SUT.addPatient(validName, "123", validNumber);
        assertTrue(result.isError());
        assertEquals(PatientService.Error.INVALID_ADDRESS, result.getError());
    }

    @Test
    void getPatientById_validId_returnsPatient() throws Exception {
        SUT.addPatient(validName, validAddress, validNumber);
        int patientId = SUT.getPatientsList().get(0).getId();
        Patient patient = SUT.getPatientById(patientId);
        assertNotNull(patient);
        assertEquals("John Doe", patient.getFullName());
    }

    @Test
    void deletePatient_nonExistentId_returnsFalse() {
        boolean isDeleted = SUT.deletePatient(9999);
        assertFalse(isDeleted);
    }

    @Test
    void addPatient_minimumValidName_succeeds() {
        var result = SUT.addPatient("Ann", validAddress, validNumber);
        assertTrue(result.isSuccess());
    }

    @Test
    void addPatient_validTelephoneFormats_succeed() {
        assertAll(
                () -> assertTrue(SUT.addPatient("John doe", validAddress,"5551234567").isSuccess()),
                () -> assertTrue(SUT.addPatient("Jane doe", validAddress, "+44555124567").isSuccess())
        );
    }

    @Test
    void addPatient_invalidAddress_returnsCorrectError() {
        var result = SUT.addPatient(validName, "", validNumber);
        assertEquals(PatientService.Error.INVALID_ADDRESS, result.getError());
    }
}