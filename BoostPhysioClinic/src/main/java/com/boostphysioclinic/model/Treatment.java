package com.boostphysioclinic.model;


public class Treatment {
    private String name;

    public Treatment(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return getName();
    }
}
