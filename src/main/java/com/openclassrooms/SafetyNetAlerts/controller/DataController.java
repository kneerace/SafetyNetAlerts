package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.ChildAlertResponse;
import com.openclassrooms.SafetyNetAlerts.model.FireResponse;
import com.openclassrooms.SafetyNetAlerts.model.FireStationServiceResponse;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    private final FireStationService fireStationService;

    @Autowired
    public DataController(FireStationService fireStationService){
        this.fireStationService = fireStationService;
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
    } // end of class

