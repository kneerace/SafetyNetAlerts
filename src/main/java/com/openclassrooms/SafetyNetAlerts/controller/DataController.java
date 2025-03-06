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
import java.util.Map;

@RestController
public class DataController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataController.class);
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

    /*@GetMapping("/firestation")
    public FireStationServiceResponse getFireStationByStationNumber(@RequestParam int stationNumber) {

        return fireStationService.getFireStationByStationNumber(stationNumber);

    } // end of method*/

    @GetMapping("/childAlert")
    public List<ChildAlertResponse> getChildByAddress(@RequestParam String address){
        logger.info("Recieved request to get childAlert for address: {}", address);
        List<ChildAlertResponse> response;
        try {
             response = fireStationService.getChildByAddress(address);
            logger.debug("ChildAlertResponse retrieved successfully: {}", response);
        } catch (Exception e) {
            logger.error("Error retrieving childAlert for address {}: {}",address,  e.getMessage(), e);
            throw e; // Re-throw the exception so it is properly handled
        }

        logger.info("Successfully processed childAlert request for address: {}", address);
        return  response;


    } // end of getChildByAddress

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersWithinFireStationJurisdiction(@RequestParam int firestation) {
        logger.info("Received request to get phoneAlert for firestation: {}", firestation);

        List<String> resposne;
        try{
            resposne = fireStationService.getPhoneNumbersWithinFireStationJurisdiction(firestation);
            logger.debug("Phone number retrieved successfully for firestation: {}", firestation);
        } catch(Exception e){
            logger.error("Error retrieving phoneAlert for firestation {}: {}", firestation, e.getMessage(), e);
            throw e;
        }
        logger.info("Successfully processed phoneAlert request for firestation {}", firestation);
        return resposne;

    } // end of getPhoneNumbersWithinFireStationJurisdiction

    @GetMapping("/fire")
    public ResponseEntity<?> getFireStationByAddress(@RequestParam String address) {
        logger.info("Received request to get fireStations for address: {}", address);
        FireResponse response;
        try {
            response = fireStationService.getFireStationByAddress(address);
            if(response == null) {
                logger.info("No fireStations found for address: {}", address);
                return ResponseEntity.ok(Collections.emptyMap());  // to return {}
            }
            logger.debug("FireResponse retrieved successfully for address: {}", address);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving fireStations for address {}: {}", address, e.getMessage(), e);
            throw e;
        }
    } // end of getFireStationByAddress

    @GetMapping("/flood/stations")
    public List<HouseholdByStation> getHouseholdByFireStations(@RequestParam List<String> stations) {
        logger.info("Received request to get household for fire stations: {}", stations);

        List<HouseholdByStation> response;
        try {
            response = floodService.getHouseholdByFireStations(stations);
            logger.debug("Household retrieved successfully for fire stations: {}", stations);
        } catch (Exception e) {
            logger.error("Error retrieving household for fire stations {}: {}", stations, e.getMessage(), e);
            throw e;
        }
        logger.info("Successfully processed household request for fire stations: {}", stations);
        return response;
    } // end of getHouseholdByFireStations


    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Received request to get PersonalInfo for firstname={} and lastname={}", firstName, lastName);
        PersonalInfoResponse response;
        try {
             response = personalService.getPersonInfo(firstName, lastName);
             if (response == null) {
                 return ResponseEntity.ok(Collections.emptyMap());
             }
            logger.debug("PersonalInfo retrieved successfully for firstname={} and lastname={}", firstName, lastName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving PersonalInfo for firstname={} and lastname={}: {}", firstName, lastName, e.getMessage(), e);
            throw e;
        }
    } // end of getPersonInfo

    @GetMapping("/communityEmail")
    public ResponseEntity<EmailListResponse> getCommunityEmail(@RequestParam String city) {
        logger.info("Received request to get communityEmail for city: {}", city);
        EmailListResponse response;
        try{
            response = personalService.getCommunityEmailList(city);
            logger.debug("EmailListResponse retrieved successfully for city: {}", city);
        } catch (Exception e) {
            logger.error("Error retrieving EmailListResponse for city {}: {}", city, e.getMessage(), e);
            throw e;
        }
        logger.info("Successfully processed communityEmail request for city: {}", city);

        return response.getEmailList().isEmpty()
//                ? ResponseEntity.notFound().build()
                ? ResponseEntity.ok(new EmailListResponse(Collections.emptyList()))  // returining empty json {}
                : ResponseEntity.ok(response);
    } // end of getCommunityEmail

    } // end of class

