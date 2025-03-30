package com.boostphysioclinic.services;


import com.boostphysioclinic.model.Physiotherapist;
import com.boostphysioclinic.model.TimetableSlot;
import com.boostphysioclinic.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class PhysiotherapistService {
    private final List<Physiotherapist> physiotherapists = new ArrayList<>();

    public void addPhysiotherapist(String fullName, String address, String tel, List<String> expertise) {
        Physiotherapist newPhysio = new Physiotherapist(IdGenerator.generatePersonnelId(), fullName, address, tel, expertise);
        physiotherapists.add(newPhysio);
    }
    public void addSlotToPhysiotherapist(Physiotherapist physio, TimetableSlot slot) {
        physio.getTimetable().add(slot);
    }

    public List<Physiotherapist> getPhysiotherapistsByName(String name) {
        List<Physiotherapist> result = new ArrayList<>();

        for (Physiotherapist physiotherapist : physiotherapists) {
            if (physiotherapist.getFullName().toLowerCase().contains(name.toLowerCase())) {
                result.add(physiotherapist);
            }
        }
        return result;
    }

    public List<Physiotherapist> getPhysiotherapistsByExpertise(String expertise) {
        List<Physiotherapist> result = new ArrayList<>();

        for (Physiotherapist physiotherapist : physiotherapists) {
            if (!physiotherapist.getExpertise().isEmpty() && physiotherapist.getExpertise().contains(expertise)) {
                result.add(physiotherapist);
            }
        }
        return result;
    }

    public List<Physiotherapist> getAllPhysiotherapists() {
        return List.copyOf(physiotherapists);
    }
}