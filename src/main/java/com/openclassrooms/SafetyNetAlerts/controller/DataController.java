package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import com.openclassrooms.SafetyNetAlerts.service.FloodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    private final FireStationService fireStationService;
    private final FloodService floodService;

    @Autowired
    public DataController(FireStationService fireStationService, FloodService floodService) {
        this.fireStationService = fireStationService;
        this.floodService =  floodService;
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
    public List<HouseholdByStation> getHouseholdByFireStations(@RequestParam List<String> fireStations) {

        return floodService.getHouseholdByFireStations(fireStations);
    } // end of getHouseholdByFireStations


    } // end of class

