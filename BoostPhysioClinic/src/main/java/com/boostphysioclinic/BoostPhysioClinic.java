package com.boostphysioclinic;

import com.boostphysioclinic.presentation.HomeScreenController;
import com.boostphysioclinic.services.TimeTableService;


/**
 * @author Chukwudalu Ibuodinma
 */
public class BoostPhysioClinic {

    public static void main(String[] args) {
        TimeTableService timeTableService = new TimeTableService();
        timeTableService.generateSampleData();
        new HomeScreenController();
    }
}

