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
class PersonalServiceTest {
    @Mock
    private LocalFileDataLoaderService dataLoaderService;

    @InjectMocks
    private PersonalService personalService;

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
//        dataLoaded.setFirestations(new ArrayList<>(Arrays.asList(
//                new FireStation("1509 Culver St", "1"),
//                new FireStation("456 Oak Ave", "2")
//        )));
        when(dataLoaderService.getDataLoaded()).thenReturn(dataLoaded);
    }

    @Test
    void getAllPersons_success() {
        List<Person> persons = personalService.getAllPersons();
        assertEquals(4, persons.size());
    }

    @Test
    void getPersonInfo_success() {
        PersonalInfoResponse response = personalService.getPersonInfo("John", "Boyd");
        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Boyd", response.getLastName());
        assertEquals("1509 Culver St", response.getAddress());
        assertEquals("jaboyd@email.com", response.getEmail());
        assertEquals(41, response.getAge());
        assertEquals(0, response.getMedicalRecords().size());
        assertEquals(0, response.getAllergies().size());
    }

    @Test
    void getPersonInfo_notFound() {
        assertNull(personalService.getPersonInfo("Non", "Existent"));
    }

    @Test
    void getCommunityEmail_success() {
        List<String> emails = personalService.getCommunityEmail("Anytown");
        assertEquals(1, emails.size());
    }

    @Test
    void getCommunityEmail_notFound() {
        List<String> emails = personalService.getCommunityEmail("Nowhere");
        assertTrue(emails.isEmpty());
    }

    @Test
    void getCommunityEmailList_success() {
        EmailListResponse response = personalService.getCommunityEmailList("Culver");
        assertEquals(3, response.getEmailList().size());
    }

    @Test
    void addPerson_success() {
        Person newPerson = new Person("New", "Person", "789 Pine Ln", "Town", "12345", "123-456-7894", "new.person@example.com");
        Person addedPerson = personalService.addPerson(newPerson);
        assertEquals(newPerson, addedPerson);
        assertEquals(5, dataLoaded.getPersons().size());
    }

    @Test
    void addPerson_alreadyExists() {
        Person existingPerson = new Person("John", "Boyd", "1509 Culver St", "Anytown", "12345", "123-456-7890", "john.doe@example.com");
        assertThrows(IllegalArgumentException.class, () -> personalService.addPerson(existingPerson));
    }

    @Test
    void updatePerson_success() {
//        Person updatedPerson = new Person("John", "Boyd", "1510 Culver St", "newtown", "54321", "987-654-3210", "updated.john.doe@example.com");

        Person updatedPerson = new Person("John", "Boyd", "1510 Culver St", "newtown", "97451", "841-874-6512", "jaboyd@email.com");

        Person response = personalService.updatePerson(updatedPerson);
//        assertEquals(updatedPerson, response);
        assertEquals("1510 Culver St", dataLoaded.getPersons().get(0).getAddress());
        assertEquals("newtown", dataLoaded.getPersons().get(0).getCity());
    }

    @Test
    void updatePerson_notFound() {
        Person nonExistentPerson = new Person("Non", "Existent", "789 Pine Ln", "Anytown", "12345", "123-456-7894", "non.existent@example.com");
        assertThrows(IllegalArgumentException.class, () -> personalService.updatePerson(nonExistentPerson));
    }

    @Test
    void deletePerson_success() {
        personalService.deletePerson("John", "Boyd");
        assertEquals(3, dataLoaded.getPersons().size());
    }

    @Test
    void deletePerson_notFound() {
        assertThrows(IllegalArgumentException.class, () -> personalService.deletePerson("Non", "Existent"));
    }
}