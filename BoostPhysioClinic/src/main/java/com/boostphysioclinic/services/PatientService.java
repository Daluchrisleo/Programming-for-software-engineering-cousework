package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Patient;
import com.boostphysioclinic.util.IdGenerator;
import com.boostphysioclinic.util.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing patients.
 * Provides functionality to add, retrieve, and delete patients,
 * and validates input data during patient creation.
 */
public class PatientService {
    private List<Patient> patients = new ArrayList<>();

    private Validator validator = new Validator();

    /**
     * Adds a new patient to the system after validating the input fields.
     *
     * @param fullName  the full name of the patient
     * @param address   the address of the patient
     * @param telephone the telephone number of the patient (digits only, may start with '+')
     * @return a {@link Result} containing the new patient ID on success,
     *         or an {@link Error} enum on failure
     */
    public Result<Patient, Error> addPatient(String fullName, String address, String telephone) {
        if (!validator.validateName(fullName)) {
            return Result.error(Error.NAME_TOO_SHORT);
        }

        if (!validator.validateAddress(address)) {
            return Result.error(Error.INVALID_ADDRESS);
        }

        if (!validator.validateTelephone(telephone)) {
            System.out.println("Invalid telephone format");
            return Result.error(Error.INVALID_TELEPHONE);
        }

        if (patients.stream().anyMatch(p -> p.getFullName().equals(fullName))){
            return Result.error(Error.PATIENT_EXISTS);
        }

        Patient patient = new Patient(IdGenerator.generatePersonnelId(), fullName, address, telephone);
        patients.add(patient);
        return Result.success(patient);
    }

    /**
     * Deletes a patient by their unique ID.
     *
     * @param id the ID of the patient to delete
     * @return true if a patient with the given ID was found and deleted, false if the patient does not exist
     */
    public boolean deletePatient(int id) {
        return patients.removeIf(p -> p.getId() == id);
    }

    /**
     * Retrieves a patient by their unique ID.
     *
     * @param id the ID of the patient to retrieve
     * @return the {@link Patient} object if found, or null if not found
     */
    public Patient getPatientById(int id) {
        return patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the list of all patients currently stored.
     *
     * @return a {@link List} of {@link Patient} objects
     */
    public List<Patient> getPatientsList() {
        return patients;
    }

    public Validator getValidator() {
        return validator;
    }

    /**
     * Enumeration of possible error states for patient operations.
     */
    public enum Error {
        NAME_TOO_SHORT,
        INVALID_TELEPHONE,
        INVALID_ADDRESS,
        PATIENT_EXISTS,
        PATIENT_NOT_FOUND,
    }

    public static class Validator{
        public boolean validateName(String fullName) {
            return fullName.length() >= 3;
        }

        public boolean validateAddress(String address) {
            return address.length() >= 4;
        }

        public boolean validateTelephone(String telephone) {
            return telephone.length() >= 7 && telephone.matches("\\+?[0-9]+");
        }
    }
}

