package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.controller.MedicalRecordController;
import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
import com.openclassrooms.SafetyNetAlerts.model.FireStation;
import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final LocalFileDataLoaderService dataLoaderService;

    @Autowired
    public MedicalRecordService(LocalFileDataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }       // end constructor

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();

        boolean medicalRecordExists = medicalRecords.stream()
                .anyMatch(medRcrd -> medRcrd.getFirstName().equals(medicalRecord.getFirstName()
                        ) && medRcrd.getLastName().equals(medicalRecord.getLastName())
                        );

        if (medicalRecordExists) {
            throw new IllegalArgumentException("Medical record for this person already exists");
        }

        medicalRecords.add(medicalRecord);
        return medicalRecord;
    } // end getMedicalRecord


}
