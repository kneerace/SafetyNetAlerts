package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.FireStationServiceResponse;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private static final Logger logger = LoggerFactory.getLogger(FireStationController.class);
    private FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    } // end of constructor

//    http://localhost:8080/firestation
//    This endpoint will provide the following via Http Post/Put/Delete: Add a firestation/address mapping.
//    Update an address’ firestation number.
//    Delete a firestation/address mapping.


    @GetMapping
    public ResponseEntity<?> getFireStationByStationNumber(@RequestParam int stationNumber) {
        logger.debug("getFireStationByStationNumber called with stationNumber: {}", stationNumber);

        FireStationServiceResponse fireStationServiceResponse;
        try{
            fireStationServiceResponse = fireStationService.getFireStationByStationNumber(stationNumber);
            if(fireStationServiceResponse == null) {
                logger.info("No fire station found for station number: {}", stationNumber);  // log response
                return  ResponseEntity.ok(Collections.emptyMap())   ;
            }
            logger.info("Fire station found for station number: {}", stationNumber);  // log response
            return ResponseEntity.ok(fireStationServiceResponse);
        } catch (Exception e) {
            logger.error("Eror retrieving fire stations: {}", e.getMessage(), e);
            throw e;
        }

    } // end of method

    @GetMapping("/all")
    public List<FireStation> getFireStations() {
        return fireStationService.getFireStations();
    } // end of getFireStations

    @PostMapping
    public ResponseEntity<?> addFireStation( @RequestBody FireStation fireStation) {
        try{
            FireStation  fireStation1 = fireStationService.addFireStation(fireStation);
            return ResponseEntity.ok(fireStation1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", e.getMessage())
            );
        }
    } // end of addFireStation

    @PutMapping
    public ResponseEntity<?> updateFireStation( @RequestBody FireStation fireStation) {
        try{
            FireStation  fireStation1 = fireStationService.updateFireStation(fireStation);
            return ResponseEntity.ok(fireStation1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", e.getMessage())
            );
        }
    } // end of updateFireStation

    @DeleteMapping
    public ResponseEntity<?> deleteFireStation( @RequestBody FireStation fireStation) {
        try{
            fireStationService.deleteFireStation(fireStation);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("message", "Fire station deleted successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", e.getMessage())
            );
        }
    } // end of deleteFireStation

} // end of FireStationController
