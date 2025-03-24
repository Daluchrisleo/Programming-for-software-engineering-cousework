package com.boostphysioclinic.model;


public abstract class Personnel {
    private int id;
    private String fullName;
    private String address;
    private String tel;

    public Personnel(int id, String fullName, String address, String tel) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }
}
