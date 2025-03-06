package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    } // end of constructor

    /*
    http://localhost:8080/medicalRecord
This endpoint will provide the following via Http Post/Put/Delete:
Add a medical record.
Update an existing medical record (as above, assume that firstName and lastName do not change).
Delete a medical record. (Use a combination of firstName and lastName as a unique identifier)

     */

    @PostMapping
    public ResponseEntity<?> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try{
            MedicalRecord addedMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);
            return new ResponseEntity<>( addedMedicalRecord, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    } // end of addMedicalRecord

    @PutMapping
    public ResponseEntity<?> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try{
            MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
            return new ResponseEntity<>( updatedMedicalRecord, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    } // end of updateMedicalRecord
} // end of class
