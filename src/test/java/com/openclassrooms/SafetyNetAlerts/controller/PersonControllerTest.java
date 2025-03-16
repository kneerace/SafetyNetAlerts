package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonControllerTest {
    @Mock
    private PersonalService personalService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPersonRecords_success() {
        List<Person> expectedPersons = Collections.singletonList(new Person());
        when(personalService.getAllPersons()).thenReturn(expectedPersons);

        ResponseEntity<?> response = personController.getAllPersonRecords();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPersons, response.getBody());
        verify(personalService, times(1)).getAllPersons();
    }

    @Test
    void getAllPersonRecords_notFound() {
        when(personalService.getAllPersons()).thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<?> response = personController.getAllPersonRecords();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
        verify(personalService, times(1)).getAllPersons();
    }

    @Test
    void addPerson_success() {
        Person person = new Person();
        when(personalService.addPerson(person)).thenReturn(person);

        ResponseEntity<?> response = personController.addPerson(person);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(person, response.getBody());
        verify(personalService, times(1)).addPerson(person);
    }

    @Test
    void addPerson_conflict() {
        Person person = new Person();
        when(personalService.addPerson(person)).thenThrow(new IllegalArgumentException("Conflict"));

        ResponseEntity<?> response = personController.addPerson(person);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", ((Map<?, ?>) response.getBody()).get("message"));
        verify(personalService, times(1)).addPerson(person);
    }

    @Test
    void updatePerson_success() {
        Person person = new Person();
        when(personalService.updatePerson(person)).thenReturn(person);

        ResponseEntity<?> response = personController.updatePerson(person);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(person, response.getBody());
        verify(personalService, times(1)).updatePerson(person);
    }

    @Test
    void updatePerson_notFound() {
        Person person = new Person();
        when(personalService.updatePerson(person)).thenThrow(new IllegalArgumentException("Not Found"));

        ResponseEntity<?> response = personController.updatePerson(person);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", ((Map<?, ?>) response.getBody()).get("message"));
        verify(personalService, times(1)).updatePerson(person);
    }

    @Test
    void deletePerson_success() {
        String firstName = "John";
        String lastName = "Doe";

        ResponseEntity<?> response = personController.deletePerson(firstName, lastName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("MedicalRecords for+John Doe deleted successfully", ((Map<?, ?>) response.getBody()).get("message"));
        verify(personalService, times(1)).deletePerson(firstName, lastName);
    }

    @Test
    void deletePerson_notFound() {
        String firstName = "John";
        String lastName = "Doe";
        doThrow(new RuntimeException("Not Found")).when(personalService).deletePerson(firstName, lastName);

        ResponseEntity<?> response = personController.deletePerson(firstName, lastName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
        verify(personalService, times(1)).deletePerson(firstName, lastName);
    }
} // end of PersonControllerTest
