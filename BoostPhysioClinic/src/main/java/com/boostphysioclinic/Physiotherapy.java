/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boostphysioclinic;

import java.util.UUID;

/**
 *
 * @author DELL
 */
public class Physiotherapy extends Patients {
    private String expertise;


    public Physiotherapy(String firstName, String lastName, String address, String phoneNumber, String expertise) {
        super(firstName, lastName, address, phoneNumber);
        this.expertise = expertise;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
