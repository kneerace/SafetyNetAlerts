package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.util.AgeCalculator;
import com.openclassrooms.SafetyNetAlerts.util.PersonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FloodService {

    private final DataLoaderService dataLoaderService;

    public FloodService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    } // end of constructor


      /*
     http://localhost:8080/flood/stations?stations=<a list of station_numbers>
    http://localhost:8080/flood/stations?stations=1,2
    This should return a list of all the households in each fire station’s jurisdiction.
 This list needs to group people by household address, include name, phone number, and age of each person,
  and any medications (with dosages) and allergies beside each person’s name.

*/

    public List<HouseholdByStation> getHouseholdByFireStations( List<String> fireStations) {
        DataLoaded dataLoaded = dataLoaderService.loadData();

        List<Person> persons = dataLoaded.getPersons();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
        List<FireStation> fireStationsList = dataLoaded.getFirestations();

//         response to store the response while iterating
        List<HouseholdByStation> response = new ArrayList<>();

        //iterate through the list of fire stations
        for (String fireStation : fireStations) {  // fireStations = [1,3]
            // getting fire station by address
            List<FireStation> filteredFireStation = fireStationsList.stream()
                    .filter(filterFirestation -> filterFirestation.getStation().equals(fireStation))
                    .collect(Collectors.toList());

            // handle if filteredFireStation is empty
            if (filteredFireStation.isEmpty()) {
//                logger.info("No fire station found for fire station number: {}" , fireStation);
                continue;  // skip to the next station
            } // end if

            Map<String, List<PersonDetails>> households = new HashMap<>();

            // iterating through filtered firestation for person and address
            for(FireStation firestation : filteredFireStation) {
                String addressCovered = firestation.getAddress();

                List<PersonDetails> personDetails = persons.stream()
                        .filter(person -> person.getAddress().equalsIgnoreCase(addressCovered))
                        .map(person -> {
                            MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
                            int age = AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy");
                            return new PersonDetails(
                                    person.getFirstName(),
                                    person.getLastName(),
                                    person.getPhone(),
                                    age,
                                    medicalRecord.getMedications(),
                                    medicalRecord.getAllergies());
                        })
                        .collect(Collectors.toList());

                households.put(addressCovered, personDetails);
            } // end for

            List<Household> householdList = households.entrySet().stream()
                    .map(entry -> new Household(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            // adding to the response
            response.add(new HouseholdByStation(fireStation, householdList));
        } // end for

        // sorting the response based on fire station
        response.sort((o1, o2) -> o1.getFireStation().compareTo(o2.getFireStation()));

        return response;
    } // end of getHouseholdByFireStations
}
