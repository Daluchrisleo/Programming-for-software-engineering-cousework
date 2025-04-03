package com.boostphysioclinic.services;

import static org.junit.jupiter.api.Assertions.*;

import com.boostphysioclinic.model.Physiotherapist;
import com.boostphysioclinic.model.TimetableSlot;
import com.boostphysioclinic.model.Treatment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


class PhysiotherapistServiceTest {
    private PhysiotherapistService service;
    private Physiotherapist physio;

    @BeforeEach
    void setUp() {
        service = new PhysiotherapistService();

        // Setup test physiotherapists
        service.addPhysiotherapist("John Doe", "123 Main St", "555-1234", List.of("Sports", "Rehab"));
        service.addPhysiotherapist("Jane Smith", "456 Oak St", "555-5678", List.of("Pediatric", "Geriatric"));

        physio = service.getAllPhysiotherapists().get(0);
    }

    @Test
    void addPhysiotherapist_shouldIncrementListSize() {
        int initialSize = service.getAllPhysiotherapists().size();
        service.addPhysiotherapist("New Physio", "Address", "Phone", List.of("Expertise"));
        assertEquals(initialSize + 1, service.getAllPhysiotherapists().size());
    }

    @Test
    void addPhysiotherapist_shouldGenerateUniqueIds() {
        service.addPhysiotherapist("Alice Brown", "789 Pine St", "555-9012", List.of("Orthopedic"));
        List<Physiotherapist> physios = service.getAllPhysiotherapists();

        long uniqueIdCount = physios.stream()
                .map(Physiotherapist::getId)
                .distinct()
                .count();

        assertEquals(physios.size(), uniqueIdCount);
    }

    @Test
    void addSlotToPhysiotherapist_shouldAddToTimetable() {
        TimetableSlot slot = new TimetableSlot(physio, new Treatment("Massage"), LocalDateTime.now());

        service.addSlotToPhysiotherapist(physio, slot);

        assertTrue(physio.getTimetable().contains(slot));
        assertEquals(1, physio.getTimetable().size());
    }

    @Test
    void addSlotToPhysiotherapist_shouldAllowMultipleSlots() {
        TimetableSlot slot1 = new TimetableSlot(physio, new Treatment("A"), LocalDateTime.now());
        TimetableSlot slot2 = new TimetableSlot(physio, new Treatment("B"), LocalDateTime.now().plusHours(1));

        service.addSlotToPhysiotherapist(physio, slot1);
        service.addSlotToPhysiotherapist(physio, slot2);

        assertEquals(2, physio.getTimetable().size());
    }

    @Test
    void getPhysiotherapistsByName_shouldReturnMatchingResults() {
        service.addPhysiotherapist("John Smith", "Address", "Phone", List.of());

        List<Physiotherapist> result = service.getPhysiotherapistsByName("John");

        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().allMatch(p -> p.getFullName().contains("John")))
        );
    }

    @Test
    void getPhysiotherapistsByExpertise_shouldFilterCorrectly() {
        service.addPhysiotherapist("Sports Specialist", "Addr", "Ph", List.of("Sports"));

        List<Physiotherapist> sportsExperts = service.getPhysiotherapistsByExpertise("Sports");

        assertAll(
                () -> assertEquals(2, sportsExperts.size()),
                () -> assertTrue(sportsExperts.stream()
                        .allMatch(p -> p.getExpertise().contains("Sports")))
        );
    }


    @Test
    void getPhysiotherapistsByExpertise_shouldHandleEmptyExpertise() {
        assertEquals(2, service.getAllPhysiotherapists().size()); // 2 physiotherapist added by default
        service.addPhysiotherapist("No Expertise", "Addr", "Ph", List.of());

        List<Physiotherapist> result = service.getPhysiotherapistsByExpertise("Sports");
        System.out.println(result.toString());
        assertEquals(1, result.size()); // Should not include the new physio
    }

    @Test
    void addPhysiotherapist_shouldHandleEmptyExpertiseList() {
        assertDoesNotThrow(() ->
                service.addPhysiotherapist("Name", "Addr", "Phone", List.of())
        );
    }

    @Test
    void getPhysiotherapistsByName_shouldBeCaseInsensitive() {
        List<Physiotherapist> result = service.getPhysiotherapistsByName("john");
        assertTrue(result.size() > 0);
    }

    @Test
    void getPhysiotherapistsByExpertise_shouldHandlePartialMatches() {
        service.addPhysiotherapist("Partial Expert", "Addr", "Ph", List.of("Sports Medicine"));
        service.addPhysiotherapist("James Expert", "Addr", "Ph", List.of("Unrelated"));

        List<Physiotherapist> result = service.getPhysiotherapistsByExpertise("Sports");
        assertEquals(1, result.size());
    }

    @Test
    void getAllPhysiotherapists_shouldReturnImmutableList() {
        List<Physiotherapist> result = service.getAllPhysiotherapists();
        assertThrows(UnsupportedOperationException.class, () ->
                result.add(new Physiotherapist(999, "Test", "Addr", "Phone", List.of()))
        );
    }
}