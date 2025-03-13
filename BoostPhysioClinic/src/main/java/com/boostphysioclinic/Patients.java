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
public class Patients {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    
    public Patients(String firstName, String lastName, String address, String phoneNumber){
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    
    public String getId(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getAddress(){
        return address;
    } 
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public String toString(){
        return "ID: " + id + ", First Name: " + firstName + ", Last Name: " + lastName + ", Address: " + address + ", Phone Number: " + phoneNumber;
    }
    
}
