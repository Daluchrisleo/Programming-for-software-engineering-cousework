package com.boostphysioclinic.presentation;

import com.boostphysioclinic.services.AppointmentService;
import com.boostphysioclinic.services.PatientService;
import com.boostphysioclinic.util.Result;

import java.util.List;

import static com.boostphysioclinic.presentation.ConsoleView.MessageType.ERROR;
import static com.boostphysioclinic.presentation.ConsoleView.MessageType.INFO;

public class HomeScreenController {
    private final ConsoleView view = new BasicConsoleView();
    private PatientService patientService = new PatientService();
    private AppointmentService appointmentService = new AppointmentService();


    public HomeScreenController() {

        String appHeader = """

                +-----------------------------------+
                |    -  Boost Physio Clinic  -      |
                +-----------------------------------+
                """;

        view.showMessage(appHeader, INFO);

        showMainMenuOptions();
    }

    private void showMainMenuOptions() {
        List<String> options = List.of(
                "Add Patient",
                "Delete Patient",
                "Change/Manage a Booking",
                "Attend a treatment appointment",
                "Print Treatment report",
                "Print Physiotherapist report");

        int selectedOptionIndex = view.showMenu(options, "Main menu", true);


        if (selectedOptionIndex >= 0 && selectedOptionIndex < options.size()){
            view.showMessage("\n----- * " + options.get(selectedOptionIndex) + " * -----\n", INFO);
        }

        switch (selectedOptionIndex) {
            case 0 -> onAddPatient();
            case 1 -> onDeletePatient();
            case 2-> onManageBooking();
            case 3-> onAttendTreatment();
            case 4-> onPrintTreatmentReport();
            case 5-> onPrintPhysiotherapistReport();
            default -> {
                exitSystem();
            }
        }
    }

    private void onAddPatient() {
        view.showMessage("Please provider the patient's details", INFO);

        String patientName = view.promptInput("Patient's Full Name", userInput -> {
                if (patientService.getValidator().validateName(userInput)) {
                    return Result.success(userInput);
                } else {
                    return Result.error("Patient's name is too short");
                }
        });


        String patientAddress = view.promptInput("Enter the Address", userInput -> {
            if (patientService.getValidator().validateAddress(userInput)) {
                return Result.success(userInput);
            } else {
                return Result.error("Address is too short");
            }
        });

        String telephone = view.promptInput("Telephone", userInput -> {
            if (patientService.getValidator().validateTelephone(userInput)) {
                return Result.success(userInput);
            } else {
                return Result.error("Please enter a valid phone number. e.g +4476810546");
            }
        });


        var result = patientService.addPatient(patientName, patientAddress, telephone);

        if (result.isSuccess()) {
            int id = result.getData();
            // todo display this nicely with chatgpt
            view.showMessage("Patient added successfully with ID: " + id, INFO);
            showReturnToMainMenuOrExit();
        } else {
            var error = result.getError();

            switch (error) {
                case NAME_TOO_SHORT -> view.showMessage("Patient name too short", ERROR);
                case INVALID_TELEPHONE -> view.showMessage("Invalid telephone number. It must be at least 7 digits", ERROR);
                case INVALID_ADDRESS -> view.showMessage("Invalid address. Address is too short", ERROR);
                case PATIENT_EXISTS -> view.showMessage("This patient has already been registered in the system", ERROR);
                default -> view.showMessage("An unexpected error occurred. Please try again", ERROR);
            }

            showReturnToMainMenuOrExit();
        }

    }

    private void onDeletePatient() {
        int patientID = view.promptInput("Please provide the patient id", userInput -> {
            try {
                int id = Integer.parseInt(userInput);
                return Result.success(id);
            } catch (NumberFormatException e) {
                return Result.error("Patient id should be just numbers");
            }
        });

        boolean isDeleted = patientService.deletePatient(patientID);

        view.showMessage(isDeleted ? "Patient deleted successfully" : "Patient not found", isDeleted ? INFO : ERROR);

        showReturnToMainMenuOrExit();
    }

    private void onAttendTreatment() {

    }

    private void onManageBooking() {
    }

    private void onPrintPhysiotherapistReport() {
    }

    private void onPrintTreatmentReport() {

    }


    private void showReturnToMainMenuOrExit(){
        int option = view.showMenu(List.of("Return to Main Menu"), "Please select an option", true);
        if (option == 0){
            showMainMenuOptions();
        } else {
            exitSystem();
        }
    }

    private void exitSystem(){
        view.showMessage("Exiting system...", INFO);
        System.exit(0);
    }
}
