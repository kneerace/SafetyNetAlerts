package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private LocalFileDataLoaderService dataLoaderService;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private DataLoaded dataLoaded;

    @BeforeEach
    void setUp() {
        dataLoaded = new DataLoaded();
//        dataLoaded.setPersons(new ArrayList<>(Arrays.asList(
//                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
//                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"),
//                new Person("Child", "Doe", "1509 Culver St", "Culver", "97451", "841-874-6512", "child.doe@email.com"),
//                new Person("Other", "Person", "456 Oak Ave", "Anytown", "12345", "123-456-7893", "other.person@email.com")
//        )));
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
    void getAllMedicalRecords_success() {
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        assertEquals(4, records.size());
    }

    @Test
    void addMedicalRecord_success() {
        MedicalRecord record = new MedicalRecord("New", "Person", "01/01/2000", Collections.emptyList(), Collections.emptyList());
        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(record);
        assertEquals(record, addedRecord);
        assertEquals(5, dataLoaded.getMedicalrecords().size());
    }

    @Test
    void addMedicalRecord_alreadyExists() {
        MedicalRecord record = new MedicalRecord("John", "Boyd", "01/01/2000", Collections.emptyList(), Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.addMedicalRecord(record));
    }

    @Test
    void updateMedicalRecord_success() {
        MedicalRecord record = new MedicalRecord("John", "Boyd", "01/01/2000", Collections.singletonList("med:10mg"), Collections.singletonList("allergy"));
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(record);
//        assertEquals(record, updatedRecord);
        assertEquals("01/01/2000", dataLoaded.getMedicalrecords().get(0).getBirthdate());
    }

    @Test
    void updateMedicalRecord_notFound() {
        MedicalRecord record = new MedicalRecord("New", "Person", "01/01/2000", Collections.emptyList(), Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.updateMedicalRecord(record));
    }

    @Test
    void deleteMedicalRecord_success() {
        assertEquals(4, dataLoaded.getMedicalrecords().size());

        medicalRecordService.deleteMedicalRecord("John", "Boyd");
        assertEquals(3, dataLoaded.getMedicalrecords().size());
        verify(dataLoaderService, times(1)).saveData(dataLoaded);
    }

    @Test
    void deleteMedicalRecord_notFound() {
        assertThrows(IllegalArgumentException.class, () -> medicalRecordService.deleteMedicalRecord("New", "Person"));
        verify(dataLoaderService, never()).saveData(any());
    }
}