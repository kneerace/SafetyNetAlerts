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
class FireStationServiceTest {

    @Mock
    private LocalFileDataLoaderService dataLoaderService;

    @InjectMocks
    private FireStationService fireStationService;

    private DataLoaded dataLoaded;

    @BeforeEach
    void setUp() {
        dataLoaded = new DataLoaded();
        dataLoaded.setPersons(Arrays.asList(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"),
                new Person("Child", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "child.doe@email.com"),
                new Person("Other", "Person", "456 Oak Ave", "Anytown", "12345", "123-456-7893", "other.person@email.com")
        ));
        dataLoaded.setMedicalrecords(Arrays.asList(
                new MedicalRecord("John", "Boyd", "03/06/1984", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Jacob", "Boyd", "03/06/1989", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Child", "Doe", "03/06/2010", Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Other", "Person", "03/06/1984", Collections.emptyList(), Collections.emptyList())
        ));
        dataLoaded.setFirestations(new ArrayList<>(Arrays.asList(
                new FireStation("1509 Culver St", "1"),
                new FireStation("456 Oak Ave", "2")
        )));
        when(dataLoaderService.getDataLoaded()).thenReturn(dataLoaded);
    }

    @Test
    void getFireStationByStationNumber_success() {
        FireStationServiceResponse response = fireStationService.getFireStationByStationNumber(1);

        assertNotNull(response);
        assertEquals(3, response.getPersonServiced().size());
        assertEquals(2, response.getNumberOfAdults());
        assertEquals(1, response.getNumberOfChildren());
    } // end getFireStationByStationNumber

    @Test
    void getFireStationByStationNumber_noPersons() {
        dataLoaded.setPersons(Collections.singletonList(new Person("Other", "Person", "456 Oak Ave", "Anytown", "12345", "123-456-7893", "other.person@example.com")));
        when(dataLoaderService.getDataLoaded()).thenReturn(dataLoaded);

        assertNull(fireStationService.getFireStationByStationNumber(1));
    }

    @Test
    void getChildByAddress_success() {
        List<ChildAlertResponse> response = fireStationService.getChildByAddress("1509 Culver St");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Child", response.get(0).getFirstName());
    }
//=----================
    @Test
    void getChildByAddress_noChildren() {
        dataLoaded.setMedicalrecords(Collections.singletonList(new MedicalRecord("John", "Doe", "03/06/1984", Collections.emptyList(), Collections.emptyList())));
        when(dataLoaderService.getDataLoaded()).thenReturn(dataLoaded);

        List<ChildAlertResponse> response = fireStationService.getChildByAddress("123 Main St");

        assertTrue(response.isEmpty());
    }

    @Test
    void getChildByAddress_noHouseholdMembers() {
        List<ChildAlertResponse> response = fireStationService.getChildByAddress("789 Pine Ln");

        assertTrue(response.isEmpty());
    }

    @Test
    void getPhoneNumbersWithinFireStationJurisdiction_success() {
        List<String> response = fireStationService.getPhoneNumbersWithinFireStationJurisdiction(1);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.contains("841-874-6512"));
        assertTrue(response.contains("841-874-6512"));
    }

    @Test
    void getFireStationByAddress_success() {
        FireResponse response = fireStationService.getFireStationByAddress("1509 Culver St");

        assertNotNull(response);
        assertEquals(1, response.getFireStaton().size());
        assertEquals("1", response.getFireStaton().get(0));
        assertEquals(3, response.getPersonDetails().size());
    }

    @Test
    void getFireStationByAddress_noPersons() {
        FireResponse response = fireStationService.getFireStationByAddress("789 Pine Ln");

        assertNull(response);
    }

    @Test
    void getFireStations_success() {
        List<FireStation> response = fireStationService.getFireStations();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void addFireStation_success() {
        FireStation newFireStation = new FireStation("789 Pine Ln", "3");
        FireStation response = fireStationService.addFireStation(newFireStation);

        assertEquals(newFireStation, response);
        assertEquals(3, dataLoaded.getFirestations().size());
    }

    @Test
    void addFireStation_alreadyExists() {
        FireStation existingFireStation = new FireStation("1509 Culver St", "1");
        assertThrows(IllegalArgumentException.class, () -> fireStationService.addFireStation(existingFireStation));
    }

    @Test
    void updateFireStation_success() {
        FireStation updatedFireStation = new FireStation("1509 Culver St", "4");
        FireStation response = fireStationService.updateFireStation(updatedFireStation);

        assertEquals(updatedFireStation, response);
        assertEquals("4", dataLoaded.getFirestations().get(0).getStation());
    }

    @Test
    void updateFireStation_notFound() {
        FireStation nonExistingFireStation = new FireStation("789 Pine Ln", "3");
        assertThrows(IllegalArgumentException.class, () -> fireStationService.updateFireStation(nonExistingFireStation));
    }

    @Test
    void deleteFireStation_success() {
        FireStation fireStationToDelete = new FireStation("1509 Culver St", "1");
        fireStationService.deleteFireStation(fireStationToDelete);

        assertEquals(1, dataLoaded.getFirestations().size());
    }

    @Test
    void deleteFireStation_notFound() {
        FireStation nonExistingFireStation = new FireStation("789 Pine Ln", "3");
        assertThrows(IllegalArgumentException.class, () -> fireStationService.deleteFireStation(nonExistingFireStation));
    }
} // end of class