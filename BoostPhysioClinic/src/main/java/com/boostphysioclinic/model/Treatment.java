package com.boostphysioclinic.model;


/**
 * Represents a type of treatment that can be scheduled in a timetable slot.
 * Each treatment has a name describing the procedure or therapy.
 */
public class Treatment {
    private String name;

    /**
     * Constructs a new {@code Treatment} with the specified name.
     *
     * @param name the name of the treatment
     */
    public Treatment(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the treatment.
     *
     * @return the treatment name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the string representation of the treatment.
     *
     * @return the treatment name
     */
    @Override
    public String toString() {
        return getName();
    }
}

