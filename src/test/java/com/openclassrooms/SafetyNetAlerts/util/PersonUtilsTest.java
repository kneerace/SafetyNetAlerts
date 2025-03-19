package com.openclassrooms.SafetyNetAlerts.util;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonUtilsTest {


    private PersonUtils personUtils = new PersonUtils();

//        Person person = new Person("John", "First", "123 Main St", "Wilmington", "12345", "123-456-7890", "7mNp9@example.com");
//        Person person1 = new Person("John", "Second", "123 Main St", "Bear", "12345", "123-456-7890", "7mNp9@example.com");

    @Test
    void isChild_trueWhenPersonIsChild() {
        Person person = new Person("John", "Second", "123 Main St", "Wilmington", "12345", "123-456-7890", "7mNp9@example.com");
        List<MedicalRecord> medicalRecords = createMedialRecords();

        assertTrue(personUtils.isChild(person, medicalRecords));

    } // end of isChild_trueWhenPersonIsChild

    @Test
    void isChild_falseWhenPersonIsNotChild() {
        Person person = new Person("John", "First", "123 Main St", "Wilmington", "12345", "123-456-7890", "7mNp9@example.com");
        List<MedicalRecord> medicalRecords = createMedialRecords();

        assertFalse(personUtils.isChild(person, medicalRecords));
    } // end of isChild_falseWhenPersonIsNotChild

    @Test
    void isChild_returnFalseIfPersonNotFound() {
        Person person = new Person("Howdy", "First", "123 Main St", "Wilmington", "12345", "123-456-7890", "7mNp9@example.com");
        List<MedicalRecord> medicalRecords = createMedialRecords();

        assertFalse(personUtils.isChild(person, medicalRecords));
    }

    @Test
    void findMedicalRecord_shouldReturnMedicalRecordWhenPersonExists() {
        Person person = new Person("John", "First", "123 Main St", "Wilmington", "12345", "123-456-7890", "7mNp9@example.com");
        List<MedicalRecord> medicalRecords = createMedialRecords();

        MedicalRecord medicalRecord = personUtils.findMedicalRecord(person, medicalRecords);

        assertNotNull(medicalRecord);
        assertEquals("John", medicalRecord.getFirstName());
        assertEquals("First", medicalRecord.getLastName());
        assertEquals("Paracetamol", medicalRecord.getMedications().get(0).toString());
        assertEquals("Aspirin", medicalRecord.getAllergies().get(0).toString());
    } // end of findMedicalRecord_shouldReturnMedicalRecordWhenPersonExists

    @Test
    void findMedicalRecord_shouldReturnNullWhenPersonDoesNotExist() {
        Person person = new Person("Howdy", "First", "123 Main St", "Wilmington", "12345", "123-456-7890", "7mNp9@example.com");
        List<MedicalRecord> medicalRecords = createMedialRecords();

        MedicalRecord medicalRecord = personUtils.findMedicalRecord(person, medicalRecords);

        assertNull(medicalRecord);
    } // end of findMedicalRecord_shouldReturnNullWhenPersonDoesNotExist/
    private List<MedicalRecord> createMedialRecords() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        // get date that is 10 years ago
        LocalDate oneYearAgo = java.time.LocalDate.now().minusYears(1);
        LocalDate twentyYearAgo = java.time.LocalDate.now().minusYears(20);

        String oneYearAgoString = oneYearAgo.format(formatter);
        String twentyYearAgoString = twentyYearAgo.format(formatter);

        medicalRecords.add(new MedicalRecord("John", "First", twentyYearAgoString, Arrays.asList("Paracetamol"), Arrays.asList("Aspirin")));
        medicalRecords.add(new MedicalRecord("John", "Second", oneYearAgoString, Arrays.asList("Iodine"), Arrays.asList("Penicillin")));
        return medicalRecords;
    }
}