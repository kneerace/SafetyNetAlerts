package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.util.AgeCalculator;
import com.openclassrooms.SafetyNetAlerts.util.PersonUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    private final LocalFileDataLoaderService dataLoaderService;

    public FireStationService(LocalFileDataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public FireStationServiceResponse getFireStationByStationNumber( int stationNumber) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.loadData();

        List<Person> persons = dataLoaded.getPersons();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
        List<FireStation> fireStations = dataLoaded.getFirestations(); // getting fire stations

        // list address served by fire station
        List<String> addressServed = fireStations.stream()
                .filter(firestation -> firestation.getStation().equals(String.valueOf(stationNumber)))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

//        if(addressServed.isEmpty()) {
//            logger.info("No fire station found for station number: {}", stationNumber);  // log response
//            return new FireStationServiceResponse();
//        }

        // get Persons based on the address collected above
        List<Person> personsServed = persons.stream()
                .filter(person -> addressServed.contains(person.getAddress()))
                .collect(Collectors.toList());

        // Mapping persons to PersonInfo for response
        List<PersonInfo> personInfos = personsServed.stream()
                .map(person -> new PersonInfo(
                        person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()
                )).collect(Collectors.toList());


        //getting adult and childred ( anyone aged 18 or younger
        int numberOfAdults = 0;
        int numberOfChildren = 0;
        for (Person person : personsServed) {
            MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);

            if (medicalRecord != null) {
                if (AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy") > 18) {
                    numberOfAdults++;
                } else {
                    numberOfChildren++;
                }
            }
        }  // end for
        return new FireStationServiceResponse(personInfos, numberOfAdults, numberOfChildren);
    } // end of getFireStationByStationNumber


}
