package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FloodServiceTest {

    @Mock
    private LocalFileDataLoaderService dataLoaderService;
    @InjectMocks
    private FloodService floodService;

    private DataLoaded dataLoaded;

    @BeforeEach
    void setUp() {
        dataLoaded = new DataLoaded();
        dataLoaded.setPersons(new ArrayList<>(Arrays.asList(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"),
                new Person("Child", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "child.doe@email.com"),
                new Person("Other", "Person", "456 Oak Ave", "Anytown", "12345", "123-456-7893", "other.person@email.com")
        )));
        dataLoaded.setMedicalrecords(new ArrayList<>(Arrays.asList(
                new MedicalRecord("John", "Boyd", "03/06/1984", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Jacob", "Boyd", "03/06/1989", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Child", "Doe", "03/06/2010", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Other", "Person", "03/06/1984", Collections.emptyList(), Collections.emptyList())
        )));
        dataLoaded.setFirestations(new ArrayList<>(Arrays.asList(
                new FireStation("1509 Culver St", "1"),
                new FireStation("456 Oak Ave", "2")
        )));
        when(dataLoaderService.getDataLoaded()).thenReturn(dataLoaded);
    }
    @Test
    void getHouseholdByFireStations_success() {
        List<String> fireStations = Arrays.asList("1", "2");
        List<HouseholdByStation> result = floodService.getHouseholdByFireStations(fireStations);

        assertNotNull(result);
        assertEquals(2, result.size());

        HouseholdByStation station1 = result.get(0);
        assertEquals("1", station1.getFireStation());
        assertEquals(1, station1.getHouseholds().size());
        assertEquals("1509 Culver St", station1.getHouseholds().get(0).getAddress());
        assertEquals(3, station1.getHouseholds().get(0).getPersonDetails().size());

        HouseholdByStation station2 = result.get(1);
        assertEquals("2", station2.getFireStation());
        assertEquals(1, station2.getHouseholds().size());
        assertEquals("456 Oak Ave", station2.getHouseholds().get(0).getAddress());
        assertEquals(1, station2.getHouseholds().get(0).getPersonDetails().size());
    } // end of getHouseholdByFireStations

    @Test
    void getHouseholdByFireStations_stationNotFound() {
        List<String> fireStations = Arrays.asList("1", "3");
        List<HouseholdByStation> result = floodService.getHouseholdByFireStations(fireStations);

        assertNotNull(result);
        assertEquals(1, result.size());

        HouseholdByStation station1 = result.get(0);
        assertEquals("1", station1.getFireStation());
    } // end of getHouseholdByFireStations

    @Test
    void getHouseholdByFireStations_emptyInput() {
        List<String> fireStations = Collections.emptyList();
        List<HouseholdByStation> result = floodService.getHouseholdByFireStations(fireStations);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    } // end of getHouseholdByFireStations
} // end of FloodServiceTest