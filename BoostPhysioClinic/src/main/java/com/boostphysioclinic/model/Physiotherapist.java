package com.boostphysioclinic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Physiotherapist, a type of Personnel, with specific expertise
 * and a schedule of available time slots.
 */
public class Physiotherapist extends Personnel {
    private final List<String> expertise;
    private final List<TimetableSlot> timetable = new ArrayList<>();

    /**
     * Constructs a Physiotherapist with the specified details.
     *
     * @param id        The unique identifier of the physiotherapist.
     * @param fullName  The full name of the physiotherapist.
     * @param address   The address of the physiotherapist.
     * @param tel       The contact telephone number of the physiotherapist.
     * @param expertise The area of expertise of the physiotherapist.
     */
    public Physiotherapist(int id, String fullName, String address, String tel, List<String> expertise) {
        super(id, fullName, address, tel);
        this.expertise = expertise;
    }

    /**
     * Returns the area of expertise of the physiotherapist.
     *
     * @return A list of string representing the areas of expertise.
     */
    public List<String> getExpertise() {
        return expertise;
    }

    /**
     * Returns the timetable of the physiotherapist.
     *
     * @return A list of timetable slots assigned to the physiotherapist.
     */
    public List<TimetableSlot> getTimetable() {
        return timetable;
    }

    @Override
    public String toString() {
        return "Physiotherapist{" +
                "expertise=" + expertise +
                ", \ntimetable=" + timetable +
                ", \nsize=" + timetable.size() +
                '}';
    }
}
