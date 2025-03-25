package com.boostphysioclinic.util;

public class IdGenerator {

    public static int generatePatientId(){
        return (int)(Math.random()*100000);
    }

    public static int generateAppointmentId(){
        return 0;
    }
}
