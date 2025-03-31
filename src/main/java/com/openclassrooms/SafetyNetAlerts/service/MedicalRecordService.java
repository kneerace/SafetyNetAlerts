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

    public List<MedicalRecord> getAllMedicalRecords() {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        return dataLoaded.getMedicalrecords();
    } // end getAllMedicalRecords


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
    } // end addMedicalRecord

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();

        //check if medical record exists with the firstName and lastname
        boolean medicalRecordExists = medicalRecords.stream()
                .anyMatch(medRcrd -> medRcrd.getFirstName().equals(medicalRecord.getFirstName()) && medRcrd.getLastName().equals(medicalRecord.getLastName())
                        );

        if (!medicalRecordExists) {
            throw new IllegalArgumentException("Medical record not found for person with " + medicalRecord.getFirstName() + " "
                    + medicalRecord.getLastName());
        }

        MedicalRecord medicalRecordToUpdate = medicalRecords.stream().filter(medRcrd -> medRcrd.getFirstName().equals(medicalRecord.getFirstName())
                        && medRcrd.getLastName().equals(medicalRecord.getLastName()))
                .findFirst()
                .orElse(null);
        medicalRecordToUpdate.setBirthdate(medicalRecord.getBirthdate());
        medicalRecordToUpdate.setMedications(medicalRecord.getMedications());
        medicalRecordToUpdate.setAllergies(medicalRecord.getAllergies());
        return medicalRecordToUpdate;
    } // end updateMedicalRecord

    public void deleteMedicalRecord(String firstName, String lastName) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();

        //check if medical record exists with the firstName and lastname
        boolean removed = medicalRecords.removeIf(
                (medRcrd -> medRcrd.getFirstName().equals(firstName)&& medRcrd.getLastName().equals(lastName)));
        if(removed){
            dataLoaderService.saveData(dataLoaded);
        }else {
            throw new IllegalArgumentException("Medical record not found for person with " + firstName + " " + lastName);
        }
    } // end deleteMedicalRecord

} // end class
