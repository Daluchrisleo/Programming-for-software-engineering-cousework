package com.boostphysioclinic.services;

import com.boostphysioclinic.model.Patient;
import com.boostphysioclinic.model.Physiotherapist;
import com.boostphysioclinic.model.TimetableSlot;
import com.boostphysioclinic.model.Treatment;
import com.boostphysioclinic.util.Result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing the timetable of physiotherapists and generating sample data.
 * Includes logic to assign treatments and create slots for physiotherapy sessions.
 */
public class TimeTableService {
    private static final LocalDate TIMETABLE_START_DATE = LocalDate.of(2025, 1, 6); // Jan 6, 2025 (Monday)

    /**
     * Weekly schedule pattern defining the working days for each week.
     * 0 = Monday, 1 = Tuesday, ..., 6 = Sunday
     */
    private static final List<List<Integer>> WEEKLY_DAYS = List.of(
            List.of(0, 2), // Week 0: Monday and Wednesday
            List.of(1, 3), // Week 1: Tuesday and Thursday
            List.of(0, 4), // Week 2: Monday and Friday
            List.of(2, 4)  // Week 3: Wednesday and Friday
    );

    private PatientService patientService;
    private PhysiotherapistService physiotherapistService;
    private AppointmentService appointmentService;
    private List<TimetableSlot> timetableSlots;

    /**
     * Constructs a new {@code TimeTableService} and retrieves service instances.
     */
    public TimeTableService() {
        patientService = ServiceLocator.getPatientService();
        physiotherapistService = ServiceLocator.getPhysiotherapistService();
        appointmentService = ServiceLocator.getAppointmentService();
        timetableSlots = new ArrayList<>();
    }

    /**
     * Generates a set of sample physiotherapists, patients, and timetabled slots for demonstration.
     */
    public void generateSampleData() {
        generatePhysiotherapists();
        generatePatients();
        generateAndAssignPhysiotherapistTimetable();
    }

    /**
     * Populates the system with a list of predefined physiotherapists and their areas of expertise.
     */
    private void generatePhysiotherapists() {
        physiotherapistService.addPhysiotherapist("Alice Smith", "123 Main St, NY", "123-456-7890",
                List.of("Sports Medicine", "Post-Op Recovery", "Manual Physiotherapy"));
        physiotherapistService.addPhysiotherapist("Bob Johnson", "456 Oak Ave, LA", "987-654-3210",
                List.of("Orthopedic Rehabilitation", "Chronic Pain Management"));
        physiotherapistService.addPhysiotherapist("Charlie Brown", "789 Pine Rd, TX", "555-123-4567",
                List.of("Neurological Disorders", "Stroke Rehabilitation"));
        physiotherapistService.addPhysiotherapist("Diana Green", "321 Elm St, FL", "444-555-6666",
                List.of("Osteopathy", "Arthritis Management"));
        physiotherapistService.addPhysiotherapist("Ethan White", "159 Maple Dr, IL", "777-888-9999",
                List.of("Pediatric Development", "Aquatic Therapy"));
        physiotherapistService.addPhysiotherapist("Fiona Black", "753 Cedar Ln, WA", "111-222-3333",
                List.of("Respiratory Physiotherapy", "Posture Correction", "Work Injury Management"));
        physiotherapistService.addPhysiotherapist("Fiona Divine", "744 Bradley Ln, WA", "101-212-3444",
                List.of("Pelvic Floor Physiotherapy", "Pediatric Physiotherapy", "Geriatric Physiotherapy"));
    }

    /**
     * Populates the system with a list of predefined patients.
     */
    private void generatePatients() {
        patientService.addPatient("John Doe", "10 Baker St, London", "+441234567890");
        patientService.addPatient("Jane Smith", "22 Oxford Rd, Manchester", "+441612345678");
        patientService.addPatient("Michael Johnson", "5 High St, Birmingham", "+441215678901");
        patientService.addPatient("Emily Davis", "7 Queen St, Liverpool", "+441517654321");
        patientService.addPatient("David Wilson", "18 King St, Glasgow", "+441412345678");
        patientService.addPatient("Sophia Martinez", "33 Victoria Rd, Edinburgh", "+441315678901");
        patientService.addPatient("James Brown", "45 Elm St, Cardiff", "+442920987654");
        patientService.addPatient("Olivia Taylor", "56 Pine Ave, Belfast", "+442890123456");
        patientService.addPatient("William Anderson", "67 Maple Rd, Leeds", "+441133445566");
        patientService.addPatient("Isabella Thomas", "78 Oak Dr, Sheffield", "+441142233445");
        patientService.addPatient("Alexander White", "89 Cedar Ln, Bristol", "+441173332211");
        patientService.addPatient("Mia Harris", "90 Birch St, Nottingham", "+441158765432");
        patientService.addPatient("Benjamin Walker", "101 Spruce Rd, Leicester", "+441162345678");
        patientService.addPatient("Charlotte Hall", "112 Fir St, Newcastle", "+441912233445");
    }

    /**
     * Maps physiotherapist expertise to relevant treatments.
     *
     * @param expertise the expertise category
     * @return a list of treatments associated with the expertise
     */
    private List<String> getTreatmentsForExpertise(String expertise) {
        return switch (expertise) {
            case "Sports Medicine" -> List.of("Sports Injury Assessment", "Athletic Recovery Session", "Health check");
            case "Post-Op Recovery" -> List.of("Surgical Rehabilitation", "Scar Tissue Management");
            case "Manual Physiotherapy" -> List.of("Joint Mobilization", "Myofascial Release");
            case "Orthopedic Rehabilitation" -> List.of("Fracture Recovery", "Joint Replacement Therapy");
            case "Chronic Pain Management" -> List.of("Pain Relief Session", "Trigger Point Therapy");
            case "Neurological Disorders" -> List.of("Neuro-muscular Re-education", "Balance Training", "Neural mobilisation");
            case "Stroke Rehabilitation" -> List.of("Post-Stroke Mobility Training", "Cognitive Rehabilitation");
            case "Osteopathy" -> List.of("Fall Prevention Session", "Mobility Maintenance");
            case "Arthritis Management" -> List.of("Joint Preservation Therapy", "Pain Management Session", "Acupuncture");
            case "Pediatric Development" -> List.of("Developmental Delay Therapy", "Motor Skills Training");
            case "Aquatic Therapy" -> List.of("Pool Rehabilitation", "Hydrotherapy Session");
            case "Respiratory Physiotherapy" -> List.of("Breathing Exercise Session", "Chest Physiotherapy");
            case "Posture Correction" -> List.of("Ergonomic Assessment", "Postural Alignment Session");
            case "Work Injury Management" -> List.of("Ergonomic Workspace Evaluation", "Injury Prevention Session", "Massage");
            case "Pelvic Floor Physiotherapy" -> List.of("Bladder Retraining Techniques", "Postpartum Rehabilitation", "Pelvic Floor Muscle Training (PFMT)");
            case "Pediatric Physiotherapy" -> List.of("Developmental Delay Therapy", "Gait Training", "Sensory Integration Therapy");
            case "Geriatric Physiotherapy" -> List.of("Fall Prevention Programs", "Osteoarthritis & Joint Pain Management", "Functional Mobility Training");
            default -> List.of("General Physiotherapy Session");
        };
    }

    /**
     * Generates and assigns non-overlapping treatment timetable slots to each physiotherapist based on their expertise.
     */
    private void generateAndAssignPhysiotherapistTimetable() {
        List<Physiotherapist> physiotherapists = physiotherapistService.getAllPhysiotherapists();
        for (Physiotherapist physio : physiotherapists) {
            List<String> expertise = physio.getExpertise();
            for (int week = 0; week < 4; week++) {
                List<Integer> daysThisWeek = WEEKLY_DAYS.get(week);

                int expertiseIndex = week % expertise.size();
                String currentExpertise = expertise.get(expertiseIndex);
                List<String> treatments = getTreatmentsForExpertise(currentExpertise);

                for (int dayOffset : daysThisWeek) {
                    LocalDate weekStart = TIMETABLE_START_DATE.plusWeeks(week);
                    LocalDate date = weekStart.plusDays(dayOffset);
                    List<LocalTime> timeSlots = createTimeSlots();

                    int treatmentIndex = 0;
                    while (treatmentIndex < treatments.size() && !timeSlots.isEmpty()) {
                        String treatmentName = treatments.get(treatmentIndex);
                        LocalTime slotTime = timeSlots.remove(0);
                        LocalDateTime dateTime = LocalDateTime.of(date, slotTime);

                        boolean timeOccupied = physio.getTimetable().stream()
                                .anyMatch(slot -> slot.getDateTime().equals(dateTime));

                        if (!timeOccupied) {
                            Treatment treatment = new Treatment(treatmentName);
                            TimetableSlot slot = new TimetableSlot(physio, treatment, dateTime);
                            physiotherapistService.addSlotToPhysiotherapist(physio, slot);
                            timetableSlots.add(slot);
                            treatmentIndex++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a list of time slots (morning and afternoon) used for scheduling treatments.
     *
     * @return a list of {@code LocalTime} representing available session times
     */
    private List<LocalTime> createTimeSlots() {
        List<LocalTime> slots = new ArrayList<>();
        slots.add(LocalTime.of(9, 0));
        slots.add(LocalTime.of(10, 0));
        slots.add(LocalTime.of(11, 0));
        slots.add(LocalTime.of(14, 0));
        slots.add(LocalTime.of(15, 0));
        slots.add(LocalTime.of(16, 0));
        return slots;
    }
}
