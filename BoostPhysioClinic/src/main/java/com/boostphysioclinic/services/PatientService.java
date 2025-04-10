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

    /**
     * Adds a new patient to the system after validating the input fields.
     *
     * @param fullName  the full name of the patient
     * @param address   the address of the patient
     * @param telephone the telephone number of the patient (digits only, may start with '+')
     * @return a {@link Result} containing the new patient ID on success,
     *         or an {@link Error} enum on failure
     */
    public Result<Integer, Error> addPatient(String fullName, String address, String telephone) {
        if (fullName.length() < 3){
            return Result.error(Error.NAME_TOO_SHORT);
        }

        if (address.length() < 4){
            return Result.error(Error.INVALID_ADDRESS);
        }

        if (telephone.length() < 7 || !telephone.matches("\\+?[0-9]+")) {
            System.out.println("Invalid telephone format");
            return Result.error(Error.INVALID_TELEPHONE);
        }

        if (patients.stream().anyMatch(p -> p.getFullName().equals(fullName))){
            return Result.error(Error.PATIENT_EXISTS);
        }

        int newId = IdGenerator.generatePersonnelId();
        patients.add(new Patient(newId, fullName, address, telephone));
        return Result.success(newId);
    }

    /**
     * Deletes a patient by their unique ID.
     *
     * @param id the ID of the patient to delete
     * @return true if a patient with the given ID was found and deleted, false otherwise
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
}

