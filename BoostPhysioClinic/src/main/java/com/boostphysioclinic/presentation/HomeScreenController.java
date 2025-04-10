package com.boostphysioclinic.presentation;

import com.boostphysioclinic.model.Patient;
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
                "Book an Appointment",
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
            case 2-> onBookAppointment();
            case 3-> onManageBooking();
            case 4-> onAttendTreatment();
            case 5-> onPrintTreatmentReport();
            case 6-> onPrintPhysiotherapistReport();
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
            Patient newPatient = result.getData();
            prettyPrintPatientConfirmation(newPatient);
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

        String successMsg = """

                +-----------------------------------+
                |       -  Patient Deleted  -       |
                +-----------------------------------+
                """;

        view.showMessage(isDeleted ? successMsg : "Patient not found", isDeleted ? INFO : ERROR);

        showReturnToMainMenuOrExit();
    }

    private void onAttendTreatment() {
        int appointmentID = view.promptInput("Please provide the Appointment id", userInput -> {
            try {
                int id = Integer.parseInt(userInput);
                return Result.success(id);
            } catch (NumberFormatException e) {
                return Result.error("Appointment id should be just numbers");
            }
        });

        var result = appointmentService.attendAppointment(appointmentID);
        if (result.isSuccess()) {
            String msg = """

                +-----------------------------------+
                |    -  Appointment Attended  -     |
                +-----------------------------------+
                """;
            view.showMessage(msg, INFO);
        } else {
            var error = result.getError();
            switch (error) {
                case APPOINTMENT_ALREADY_ATTENDED -> {
                    view.showMessage("Appointment already attended", ERROR);
                }
                case APPOINTMENT_CANCELLED -> {
                    view.showMessage("This appointment has been cancelled. Try rebooking it", ERROR);
                }
                case APPOINTMENT_NOT_FOUND -> {
                    view.showMessage("Appointment not found", ERROR);
                }
                case CANNOT_CANCEL_ATTENDED_APPOINTMENT -> {
                    view.showMessage("The patient has already attended this appointment", ERROR);
                }
            }

            showReturnToMainMenuOrExit();
        }
    }

    private void onBookAppointment() {

    }

    private void onManageBooking() {
        List<String> options = List.of("Cancel Appointment", "Rebook Appointment", "Main menu");

        int selectedOptionIndex = view.showMenu(options, "Manage Appointment", true);

        if (selectedOptionIndex == 2){
            showReturnToMainMenuOrExit();
            return;
        }

        if (selectedOptionIndex == 0 || selectedOptionIndex == 1){
            int appointmentID = view.promptInput("Please provide the Appointment id", userInput -> {
                try {
                    int id = Integer.parseInt(userInput);
                    return Result.success(id);
                } catch (NumberFormatException e) {
                    return Result.error("Appointment id should be just numbers");
                }
            });
            if (selectedOptionIndex == 0){
                cancelAppointment(appointmentID);
            } else {
                rebookAppointment(appointmentID);
            }

        } else {
            exitSystem();
        }

    }

    private void rebookAppointment(int appointmentID) {
        var result = appointmentService.rebookAppointment(appointmentID);

        if (result.isSuccess()) {
            view.showMessage(prettyPrintMessage("✅ Appointment rebooked"), INFO);
        } else {
            var error = result.getError();
            switch (error) {
                case APPOINTMENT_NOT_FOUND -> view.showMessage("No appointment found with the provided id", ERROR);
                case APPOINTMENT_NOT_CANCELLED -> view.showMessage("This appointment is not cancelled", ERROR);
                case APPOINTMENT_SLOT_NO_LONGER_AVAILABLE ->
                        view.showMessage("This slot for this appointment is no longer available. Please try booking a new appointment", ERROR);
                case PATIENT_HAS_ANOTHER_APPOINTMENT_AT_SAME_TIME ->
                        view.showMessage("Patient has another appointment for the same time", ERROR);
            }
        }

        showReturnToMainMenuOrExit();
    }

    private void cancelAppointment(int appointmentID) {
        var result = appointmentService.cancelAppointment(appointmentID);

        if (result.isSuccess()) {
            view.showMessage(prettyPrintMessage("Appointment cancelled"), INFO);
        } else {
            var error = result.getError();
            switch (error) {
                case APPOINTMENT_ALREADY_ATTENDED -> view.showMessage("We cannot cancel an already attended appointment", ERROR);
                case APPOINTMENT_NOT_FOUND -> view.showMessage("No appointment found with the provided id", ERROR);
                case CANNOT_CANCEL_ATTENDED_APPOINTMENT -> view.showMessage("This appointment has already been cancelled", ERROR);
                default -> view.showMessage("An unexpected error occurred", ERROR);
            }
        }

        showReturnToMainMenuOrExit();
    }

    private void onPrintPhysiotherapistReport() {
    }

    private void onPrintTreatmentReport() {

    }

    public void prettyPrintPatientConfirmation(Patient patient) {
        String message = new StringBuilder()
                .append("\n+-------------------------------------------------+\n")
                .append("|             ✅ Patient Added Successfully        |\n")
                .append("+-------------------------------------------------+\n")
                .append(String.format("| ID: %-44s |\n", patient.getId()))
                .append(String.format("| Full Name: %-37s |\n", patient.getFullName()))
                .append(String.format("| Address: %-39s |\n", patient.getAddress()))
                .append(String.format("| Telephone: %-37s |\n", patient.getTel()))
                .append("+-------------------------------------------------+\n")
                .toString();

        view.showMessage(message + patient.getId(), INFO);
    }

    public String prettyPrintMessage(String message) {
        int boxWidth = 49; // Width inside the borders
        String border = "+-------------------------------------------------+\n";

        // Center the message
        int paddingSize = (boxWidth - message.length()) / 2;
        String padding = " ".repeat(Math.max(0, paddingSize));
        String line = "|"
                + padding
                + message
                + " ".repeat(Math.max(0, boxWidth - paddingSize - message.length()))
                + "|\n";

        return new StringBuilder()
                .append(border)
                .append(line)
                .append(border)
                .toString();
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
