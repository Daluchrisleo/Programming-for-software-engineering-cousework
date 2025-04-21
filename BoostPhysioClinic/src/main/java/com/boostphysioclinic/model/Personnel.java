package com.boostphysioclinic.model;

/**
 * An abstract class representing personnel within the system.
 * Serves as a base for specific types of personnel such as patients or staff,
 * providing common fields and access methods.
 */
public abstract class Personnel {
    private int id;
    private String fullName;
    private String address;
    private String tel;

    /**
     * Constructs a Personnel instance with the specified attributes.
     *
     * @param id       the unique identifier for the personnel
     * @param fullName the full name of the personnel
     * @param address  the address of the personnel
     * @param tel      the telephone number of the personnel
     */
    public Personnel(int id, String fullName, String address, String tel) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.tel = tel;
    }

    /**
     * Returns the unique ID of the personnel.
     *
     * @return the ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the full name of the personnel.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the address of the personnel.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the telephone number of the personnel.
     *
     * @return the telephone number
     */
    public String getTel() {
        return tel;
    }
}