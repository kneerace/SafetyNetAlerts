package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.service.FireStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/firestation")
public class FireStationController {
    private FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    } // end of constructor

//    http://localhost:8080/firestation
//    This endpoint will provide the following via Http Post/Put/Delete: Add a firestation/address mapping.
//    Update an address’ firestation number.
//    Delete a firestation/address mapping.

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
    }

} // end of FireStationController
