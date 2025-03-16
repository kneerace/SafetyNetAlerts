package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
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

class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMedicalRecord_success() {
        List<MedicalRecord> expectedRecords = Collections.singletonList(new MedicalRecord());
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(expectedRecords);

        ResponseEntity<?> response = medicalRecordController.getMedicalRecord();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRecords, response.getBody());
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    @Test
    void getMedicalRecord_notFound() {
        when(medicalRecordService.getAllMedicalRecords()).thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<?> response = medicalRecordController.getMedicalRecord();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    @Test
    void addMedicalRecord_success() {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(medicalRecord);

        ResponseEntity<?> response = medicalRecordController.addMedicalRecord(medicalRecord);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(medicalRecord, response.getBody());
        verify(medicalRecordService, times(1)).addMedicalRecord(medicalRecord);
    }

    @Test
    void addMedicalRecord_badRequest() {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenThrow(new RuntimeException("Bad Request"));

        ResponseEntity<?> response = medicalRecordController.addMedicalRecord(medicalRecord);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody());
        verify(medicalRecordService, times(1)).addMedicalRecord(medicalRecord);
    }

    @Test
    void updateMedicalRecord_success() {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(medicalRecord);

        ResponseEntity<?> response = medicalRecordController.updateMedicalRecord(medicalRecord);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medicalRecord, response.getBody());
        verify(medicalRecordService, times(1)).updateMedicalRecord(medicalRecord);
    }

    @Test
    void updateMedicalRecord_notFound() {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<?> response = medicalRecordController.updateMedicalRecord(medicalRecord);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
        verify(medicalRecordService, times(1)).updateMedicalRecord(medicalRecord);
    }

    @Test
    void deleteMedicalRecord_success() {
        String firstName = "John";
        String lastName = "Doe";

        ResponseEntity<?> response = medicalRecordController.deleteMedicalRecord(firstName, lastName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("MedicalRecords for John Doe deleted successfully", ((Map<?, ?>) response.getBody()).get("message"));
        verify(medicalRecordService, times(1)).deleteMedicalRecord(firstName, lastName);
    }

    @Test
    void deleteMedicalRecord_notFound() {
        String firstName = "John";
        String lastName = "Doe";
        doThrow(new RuntimeException("Not Found")).when(medicalRecordService).deleteMedicalRecord(firstName, lastName);

        ResponseEntity<?> response = medicalRecordController.deleteMedicalRecord(firstName, lastName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody());
        verify(medicalRecordService, times(1)).deleteMedicalRecord(firstName, lastName);
    }
}