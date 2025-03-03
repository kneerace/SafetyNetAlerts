package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.FireStationServiceResponse;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final FireStationService fireStationService;

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    public DataController(FireStationService fireStationService){
        this.fireStationService = fireStationService;
    }

    @GetMapping("/firestation")
    public FireStationServiceResponse getFireStationByStationNumber( int stationNumber) {

//        logger.info ("Request  received : /firestation?stationNumber={}", stationNumber);

        FireStationServiceResponse fireStationServiceResponse = fireStationService.getFireStationByStationNumber(stationNumber);
        if(fireStationServiceResponse !=null){
//            logger.info("Response: {}", fireStationServiceResponse.getPersonServiced());

        }
        else {
//            logger.warn("No data found for stationNumber: {}", stationNumber);
        }

        return fireStationServiceResponse;
    } // end of method
} // end of class

