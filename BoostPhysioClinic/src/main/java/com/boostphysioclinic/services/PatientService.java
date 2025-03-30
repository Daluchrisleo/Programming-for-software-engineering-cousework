package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Patient;
import com.boostphysioclinic.util.IdGenerator;
import com.boostphysioclinic.util.Result;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private List<Patient> patients = new ArrayList<>();

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

    public boolean deletePatient(int id) {
        return patients.removeIf(p -> p.getId() == id);
    }

    public Patient getPatientById(int id) {
        return patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Patient> getPatientsList() {
        return patients;
    }

    public enum Error {
        NAME_TOO_SHORT,
        INVALID_TELEPHONE,
        INVALID_ADDRESS,
        PATIENT_EXISTS,
        PATIENT_NOT_FOUND,
    }
}
