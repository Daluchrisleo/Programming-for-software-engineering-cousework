package com.boostphysioclinic.services;


import com.boostphysioclinic.model.Physiotherapist;
import com.boostphysioclinic.model.TimetableSlot;
import com.boostphysioclinic.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing physiotherapists in the system.
 * Allows adding physiotherapists, assigning timetable slots, and searching by name or expertise.
 */
public class PhysiotherapistService {
    private final List<Physiotherapist> physiotherapists = new ArrayList<>();

    /**
     * Adds a new physiotherapist to the system.
     *
     * @param fullName  the full name of the physiotherapist
     * @param address   the address of the physiotherapist
     * @param tel       the telephone number of the physiotherapist
     * @param expertise a list of areas of expertise (e.g., sports therapy, orthopedic rehab)
     */
    public void addPhysiotherapist(String fullName, String address, String tel, List<String> expertise) {
        Physiotherapist newPhysio = new Physiotherapist(
                IdGenerator.generatePersonnelId(), fullName, address, tel, expertise);
        physiotherapists.add(newPhysio);
    }

    /**
     * Adds a timetable slot to the given physiotherapist's schedule.
     *
     * @param physio the physiotherapist to whom the slot is being added
     * @param slot   the timetable slot to add
     */
    public void addSlotToPhysiotherapist(Physiotherapist physio, TimetableSlot slot) {
        physio.getTimetable().add(slot);
    }

    /**
     * Searches for physiotherapists whose names contain the given search term (case-insensitive).
     *
     * @param name the search term to match against physiotherapist names
     * @return a list of matching physiotherapists
     */
    public List<Physiotherapist> getPhysiotherapistsByName(String name) {
        List<Physiotherapist> result = new ArrayList<>();

        for (Physiotherapist physiotherapist : physiotherapists) {
            if (physiotherapist.getFullName().toLowerCase().contains(name.toLowerCase())) {
                result.add(physiotherapist);
            }
        }
        return result;
    }

    /**
     * Searches for physiotherapists who have the given expertise.
     *
     * @param expertise the expertise to search for
     * @return a list of physiotherapists with the specified expertise
     */
    public List<Physiotherapist> getPhysiotherapistsByExpertise(String expertise) {
        List<Physiotherapist> result = new ArrayList<>();

        for (Physiotherapist physiotherapist : physiotherapists) {
            for (String e : physiotherapist.getExpertise()) {
                if (e.toLowerCase().contains(expertise.toLowerCase())) {
                    result.add(physiotherapist);
                }
            }
        }
        return result;
    }

    /**
     * Returns an unmodifiable list of all registered physiotherapists.
     *
     * @return a list of all physiotherapists
     */
    public List<Physiotherapist> getAllPhysiotherapists() {
        return List.copyOf(physiotherapists);
    }
}