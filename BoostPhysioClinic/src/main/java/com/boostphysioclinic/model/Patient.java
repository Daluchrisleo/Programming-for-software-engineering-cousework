package com.boostphysioclinic.model;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Personnel {
    private final List<Integer> appointments = new ArrayList<>();

    public Patient(int id, String fullName, String address, String tel) {
        super(id, fullName, address, tel);
    }

    public List<Integer> getAppointments() {
        return appointments;
    }

    public void addAppointment(int appointmentId) {
        appointments.add(appointmentId);
    }

    public void removeAppointment(int appointmentId) {
        appointments.remove(appointmentId);
    }


}