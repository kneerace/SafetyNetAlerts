package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.service.FloodService;
import com.openclassrooms.SafetyNetAlerts.service.PersonalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class DataControllerTest {

    @Mock
    private FireStationService fireStationService;
    @Mock
    private FloodService floodService;
    @Mock
    private PersonalService personalService;
    @InjectMocks
    private DataController dataController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChildByAddress_success() {
        String address = "123 Main St";
        ChildAlertResponse childAlertResponse = new ChildAlertResponse("Test", "Last", 11, Collections.emptyList());

        List<ChildAlertResponse> expectedResponse = Collections.singletonList(childAlertResponse);
        when(fireStationService.getChildByAddress(address)).thenReturn(expectedResponse);
        List<ChildAlertResponse> actualResponse = dataController.getChildByAddress(address);

        assertEquals(expectedResponse, actualResponse);
        verify(fireStationService, times(1)).getChildByAddress(address);
    } //end getChildByAddress

    @Test
    void getChildByAddress_exception() {
        String address = "123 Main St";
        when(fireStationService.getChildByAddress(address)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> dataController.getChildByAddress(address));
        verify(fireStationService, times(1)).getChildByAddress(address);
    } //end getChildByAddress

    @Test
    void getPhoneNumbersWithinFireStationJurisdiction_success() {
        int firestation = 1;
        List<String> expectedResponse = Collections.singletonList("1234567890");
        when(fireStationService.getPhoneNumbersWithinFireStationJurisdiction(firestation)).thenReturn(expectedResponse);

        List<String> actualResponse = dataController.getPhoneNumbersWithinFireStationJurisdiction(firestation);

        assertEquals(expectedResponse, actualResponse);
        verify(fireStationService, times(1)).getPhoneNumbersWithinFireStationJurisdiction(firestation);
    }

    @Test
    void getPhoneNumbersWithinFireStationJurisdiction_exception() {
        int firestation = 1;
        when(fireStationService.getPhoneNumbersWithinFireStationJurisdiction(firestation)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> dataController.getPhoneNumbersWithinFireStationJurisdiction(firestation));
        verify(fireStationService, times(1)).getPhoneNumbersWithinFireStationJurisdiction(firestation);
    }

    @Test
    void getFireStationByAddress_success() {
        String address = "123 Main St";
        FireResponse expectedResponse = new FireResponse(Collections.emptyList(), Collections.emptyList());
        when(fireStationService.getFireStationByAddress(address)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = dataController.getFireStationByAddress(address);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(fireStationService, times(1)).getFireStationByAddress(address);
    }

    @Test
    void getFireStationByAddress_notFound() {
        String address = "123 Main St";
        when(fireStationService.getFireStationByAddress(address)).thenReturn(null);

        ResponseEntity<?> responseEntity = dataController.getFireStationByAddress(address);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyMap(), responseEntity.getBody());
        verify(fireStationService, times(1)).getFireStationByAddress(address);
    }

    @Test
    void getFireStationByAddress_exception() {
        String address = "123 Main St";
        when(fireStationService.getFireStationByAddress(address)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> dataController.getFireStationByAddress(address));
        verify(fireStationService, times(1)).getFireStationByAddress(address);
    }

    @Test
    void getHouseholdByFireStations_success() {
        List<String> stations = Arrays.asList("1", "2");
        List<HouseholdByStation> expectedResponse = Collections.singletonList(new HouseholdByStation("2", Collections.emptyList()));
        when(floodService.getHouseholdByFireStations(stations)).thenReturn(expectedResponse);

        List<HouseholdByStation> actualResponse = dataController.getHouseholdByFireStations(stations);

        assertEquals(expectedResponse, actualResponse);
        verify(floodService, times(1)).getHouseholdByFireStations(stations);
    }

    @Test
    void getHouseholdByFireStations_exception() {
        List<String> stations = Arrays.asList("1", "2");
        when(floodService.getHouseholdByFireStations(stations)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> dataController.getHouseholdByFireStations(stations));
        verify(floodService, times(1)).getHouseholdByFireStations(stations);
    }

    @Test
    void getPersonInfo_success() {
        String firstName = "John";
        String lastName = "Doe";
        PersonalInfoResponse expectedResponse = new PersonalInfoResponse();
        when(personalService.getPersonInfo(firstName, lastName)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = dataController.getPersonInfo(firstName, lastName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(personalService, times(1)).getPersonInfo(firstName, lastName);
    }

    @Test
    void getPersonInfo_notFound() {
        String firstName = "John";
        String lastName = "Doe";
        when(personalService.getPersonInfo(firstName, lastName)).thenReturn(null);

        ResponseEntity<?> responseEntity = dataController.getPersonInfo(firstName, lastName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyMap(), responseEntity.getBody());
        verify(personalService, times(1)).getPersonInfo(firstName, lastName);
    }

    @Test
    void getPersonInfo_exception() {
        String firstName = "John";
        String lastName = "Doe";
        when(personalService.getPersonInfo(firstName, lastName)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> dataController.getPersonInfo(firstName, lastName));
        verify(personalService, times(1)).getPersonInfo(firstName, lastName);
    }

    @Test
    void getCommunityEmail_success() {
        String city = "Anytown";
        EmailListResponse expectedResponse = new EmailListResponse(Collections.singletonList("test@example.com"));
        when(personalService.getCommunityEmailList(city)).thenReturn(expectedResponse);

        ResponseEntity<EmailListResponse> responseEntity = dataController.getCommunityEmail(city);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(personalService, times(1)).getCommunityEmailList(city);
    }

    @Test
    void getCommunityEmail_notFound() {
        String city = "Anytown";
        EmailListResponse expectedResponse = new EmailListResponse(Collections.emptyList());
        when(personalService.getCommunityEmailList(city)).thenReturn(expectedResponse);

        ResponseEntity<EmailListResponse> responseEntity = dataController.getCommunityEmail(city);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(expectedResponse.getEmailList(), responseEntity.getBody().getEmailList());
        verify(personalService, times(1)).getCommunityEmailList(city);
    } // end of getCommunityEmail_notFound

    @Test
    void getCommunityEmail_exception() {
        String city = "Anytown";
        when(personalService.getCommunityEmailList(city)).thenThrow(new RuntimeException("Test Exception"));

        assertThrows(RuntimeException.class, () -> dataController.getCommunityEmail(city));
        verify(personalService, times(1)).getCommunityEmailList(city);
    } // end of getCommunityEmail_exception

} // end of class