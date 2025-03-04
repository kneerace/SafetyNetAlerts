package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.service.FloodService;
import com.openclassrooms.SafetyNetAlerts.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class DataController {

    private final FireStationService fireStationService;
    private final FloodService floodService;
    private final PersonalService personalService;

    @Autowired
    public DataController(FireStationService fireStationService, FloodService floodService,
                          PersonalService personalService) {
        this.fireStationService = fireStationService;
        this.floodService =  floodService;
        this.personalService = personalService;
    }

    @GetMapping("/firestation")
    public FireStationServiceResponse getFireStationByStationNumber( int stationNumber) {

        return fireStationService.getFireStationByStationNumber(stationNumber);

    } // end of method

    @GetMapping("/childAlert")
    public List<ChildAlertResponse> getChildByAddress(@RequestParam String address){
        return fireStationService.getChildByAddress(address);
    } // end of getChildByAddress

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersWithinFireStationJurisdiction(@RequestParam int firestation) {

        return fireStationService.getPhoneNumbersWithinFireStationJurisdiction(firestation);

    } // end of getPhoneNumbersWithinFireStationJurisdiction

    @GetMapping("/fire")
    public FireResponse getFireStationByAddress(@RequestParam String address) {

        return fireStationService.getFireStationByAddress(address);
    } // end of getFireStationByAddress

    @GetMapping("/flood/stations")
    public List<HouseholdByStation> getHouseholdByFireStations(@RequestParam List<String> stations) {

        return floodService.getHouseholdByFireStations(stations);
    } // end of getHouseholdByFireStations


    @GetMapping("/personInfo")
    public PersonalInfoResponse getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
//        logger.info("Request: /personInfo?firstname={}&lastname={}", firstName, lastName);
        PersonalInfoResponse response = personalService.getPersonInfo(firstName, lastName);
//        logger.info("Response: {}", response);
        return response;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<EmailListResponse> getCommunityEmail(@RequestParam String city) {
//        logger.info("Request: /communityEmail?city={}", city);
//        List<String> emails = personalInfoService.getCommunityEmail(city);
//        logger.info("Emails for city: {} are: {}", city, emails);
        EmailListResponse emails = personalService.getCommunityEmailList(city);
//        return emails;
        return emails.getEmailList().isEmpty()
//                ? ResponseEntity.notFound().build()
                ? ResponseEntity.ok(new EmailListResponse(Collections.emptyList()))  // returining empty json {}
                : ResponseEntity.ok(emails);
    } // end of getCommunityEmail

    } // end of class

