package com.openclassrooms.SafetyNetAlerts.util;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;

import java.util.List;

public class PersonUtils {

    public static boolean isChild(Person person, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .anyMatch(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName() )
                        && medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())
                        && AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy") < 18);
    } // end of isChild

    public static MedicalRecord findMedicalRecord(Person person, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && medicalRecord.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .orElse(null);
    } // end of findMedicalRecord

} // end of PersonUtils
