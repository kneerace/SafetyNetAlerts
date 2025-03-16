package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.FireStationServiceResponse;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
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

class FireStationControllerTest {

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private FireStationController fireStationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFireStationByStationNumber_success() {
        Integer stationNumber = 1;
        FireStationServiceResponse fireStationServiceResponse = new FireStationServiceResponse();

        when(fireStationService.getFireStationByStationNumber(stationNumber)).thenReturn(fireStationServiceResponse);

        ResponseEntity<?> response = fireStationController.getFireStationByStationNumber(stationNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fireStationServiceResponse, response.getBody());
        verify(fireStationService, times(1)).getFireStationByStationNumber(stationNumber);
    } //end getFireStationByStationNumber_success
    @Test
    void getFireStationByStationNumber_notFound() {
        int stationNumber = 1;
        when(fireStationService.getFireStationByStationNumber(stationNumber)).thenReturn(null);

        ResponseEntity<?> responseEntity = fireStationController.getFireStationByStationNumber(stationNumber);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyMap(), responseEntity.getBody());
        verify(fireStationService, times(1)).getFireStationByStationNumber(stationNumber);
    } //end getFireStationByStationNumber_notFound

    @Test
    void getFireStationByStationNumber_exception() {
        int stationNumber = 1;
        when(fireStationService.getFireStationByStationNumber(stationNumber)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> fireStationController.getFireStationByStationNumber(stationNumber));
        verify(fireStationService, times(1)).getFireStationByStationNumber(stationNumber);
    } //end getFireStationByStationNumber_exception
    @Test
    void getFireStations_success() {
        List<FireStation> expectedResponse = Collections.singletonList(new FireStation());
        when(fireStationService.getFireStations()).thenReturn(expectedResponse);

        List<FireStation> actualResponse = fireStationController.getFireStations();

        assertEquals(expectedResponse, actualResponse);
        verify(fireStationService, times(1)).getFireStations();
    }

    @Test
    void addFireStation_success() {
        FireStation fireStation = new FireStation();
        when(fireStationService.addFireStation(fireStation)).thenReturn(fireStation);

        ResponseEntity<?> responseEntity = fireStationController.addFireStation(fireStation);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(fireStation, responseEntity.getBody());
        verify(fireStationService, times(1)).addFireStation(fireStation);
    }

    @Test
    void addFireStation_conflict() {
        FireStation fireStation = new FireStation();
        when(fireStationService.addFireStation(fireStation)).thenThrow(new RuntimeException("Conflict"));

        ResponseEntity<?> responseEntity = fireStationController.addFireStation(fireStation);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Conflict", ((Map<?, ?>) responseEntity.getBody()).get("message"));
        verify(fireStationService, times(1)).addFireStation(fireStation);
    }

    @Test
    void updateFireStation_success() {
        FireStation fireStation = new FireStation();
        when(fireStationService.updateFireStation(fireStation)).thenReturn(fireStation);

        ResponseEntity<?> responseEntity = fireStationController.updateFireStation(fireStation);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(fireStation, responseEntity.getBody());
        verify(fireStationService, times(1)).updateFireStation(fireStation);
    }

    @Test
    void updateFireStation_conflict() {
        FireStation fireStation = new FireStation();
        when(fireStationService.updateFireStation(fireStation)).thenThrow(new RuntimeException("Conflict"));

        ResponseEntity<?> responseEntity = fireStationController.updateFireStation(fireStation);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Conflict", ((Map<?, ?>) responseEntity.getBody()).get("message"));
        verify(fireStationService, times(1)).updateFireStation(fireStation);
    }

    @Test
    void deleteFireStation_success() {
        FireStation fireStation = new FireStation();

        ResponseEntity<?> responseEntity = fireStationController.deleteFireStation(fireStation);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Fire station deleted successfully", ((Map<?, ?>) responseEntity.getBody()).get("message"));
        verify(fireStationService, times(1)).deleteFireStation(fireStation);
    }

    @Test
    void deleteFireStation_conflict() {
        FireStation fireStation = new FireStation();
        doThrow(new RuntimeException("Conflict")).when(fireStationService).deleteFireStation(fireStation);

        ResponseEntity<?> responseEntity = fireStationController.deleteFireStation(fireStation);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Conflict", ((Map<?, ?>) responseEntity.getBody()).get("message"));
        verify(fireStationService, times(1)).deleteFireStation(fireStation);
    }
}