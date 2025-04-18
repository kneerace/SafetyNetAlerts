package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.util.AgeCalculator;
import com.openclassrooms.SafetyNetAlerts.util.PersonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    private static final Logger logger = LoggerFactory.getLogger(FireStationService.class);
    private final LocalFileDataLoaderService dataLoaderService;

    @Autowired
    public FireStationService(LocalFileDataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public FireStationServiceResponse getFireStationByStationNumber( int stationNumber) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();

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
        if(numberOfAdults == 0 && numberOfChildren == 0) {
            logger.info("No person found for station number: {}", stationNumber);  // log response
            return null;
        }
        return new FireStationServiceResponse(personInfos, numberOfAdults, numberOfChildren);
    } // end of getFireStationByStationNumber

    public List<ChildAlertResponse> getChildByAddress( String address){
        logger.debug("Fetching childAlert for address: {}", address);

        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();

        List<Person> persons = dataLoaded.getPersons();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();

        List<Person> houseHoldMembers = persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();
        logger.debug("Total Household members found at {}: {}", address, houseHoldMembers.size());

        if(houseHoldMembers.isEmpty()) {
            logger.warn("No household members found at: {}", address);
            return Collections.emptyList(); // return empty list instead of null
        }
        // getting children i.e. anyone under the age of 18
        List<ChildAlertResponse>  children = houseHoldMembers.stream()
//                .filter(person -> person.getAddress().equalsIgnoreCase(address) && PersonUtils.isChild(person, medicalRecords))
                .map(person -> {
                    MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
                    if (medicalRecord != null){
                        if (PersonUtils.isChild(person, medicalRecords)) {
                            return new ChildAlertResponse(person.getFirstName(), person.getLastName(),
                                    AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy"), houseHoldMembers);
                        }
                    }
                    return null;
                })
                .filter(child -> child != null)  // removing nulls
                .collect(Collectors.toList());  // converting to list
        logger.info("Total Children found at {}: {}", address, children.size());

        if(children.isEmpty()) {
            logger.warn("No children found at: {}", address);
        }

        return children;
    } // end of getChildByAddress

    /*
    http://localhost:8080/phoneAlert?firestation=<firestation_number>
    This URL should return a list of phone numbers of each person within the fire station’s jurisdiction.
    We’ll use this to send emergency text messages to specific households.
    */
    public List<String> getPhoneNumbersWithinFireStationJurisdiction( int firestation) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();

        List<Person> persons = dataLoaded.getPersons();
        List<FireStation> fireStations = dataLoaded.getFirestations(); // getting fire stations

//        getting Persons based on the address reflected in the fire station
        List<Person> personsServed = persons.stream()
                .filter(person -> fireStations.stream()
                        .filter(firestation1 -> firestation1.getStation().equals(String.valueOf(firestation)))
                        .map(FireStation::getAddress)
                        .collect(Collectors.toList())
                        .contains(person.getAddress()))
                .collect(Collectors.toList());

        List<String> phoneNumbers = personsServed.stream()
                .map(Person::getPhone)
                .collect(Collectors.toSet()) // to remove duplicates
                .stream()
                .collect(Collectors.toList());  // converting to list
        return phoneNumbers;

    } // end of getPhoneNumbersWithinFireStationJurisdiction

      /*http://localhost:8080/fire?address=<address>
    This URL should return the fire station number that services the provided address as well as a list
    of all of the people living at the address. This list should include each person’s name, phone number,
     age, medications with dosage, and allergies.
    */

    public FireResponse getFireStationByAddress( String address) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();

        List<Person> persons = dataLoaded.getPersons();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
        List<FireStation> fireStations = dataLoaded.getFirestations(); // getting fire stations

        // getting fire station by address
        List<String> fireStationsNumber = fireStations.stream()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
                .map(FireStation::getStation)
                .collect(Collectors.toList());

        // getting Persons based on the address, with medical and allergies
        List<PersonDetails> personDetails = persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
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
        if (personDetails.isEmpty()) {
            logger.warn("No person found at: {}", address);
            return null;
        }
        //return the response
        return new FireResponse(fireStationsNumber, personDetails);
    } // end of getFireStationByAddress

//    http://localhost:8080/firestation
//    This endpoint will provide the following via Http Post/Put/Delete: Add a firestation/address mapping.
//    Update an address’ firestation number.
//    Delete a firestation/address mapping.


    public List<FireStation> getFireStations() {
        // getting data from the service into DataLoaded POJO
        return dataLoaderService.getDataLoaded().getFirestations();
    } // end of getFireStations

    public FireStation addFireStation(FireStation fireStation) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<FireStation> fireStations = dataLoaded.getFirestations();

        boolean fireStationExists = fireStations.stream()
                .anyMatch(firestation -> firestation.getAddress().equals(fireStation.getAddress())
                        && firestation.getStation().equals(fireStation.getStation()));

        if (fireStationExists) {
            throw new IllegalArgumentException("Fire station already exists");
        }

        fireStations.add(fireStation);
        return fireStation;
    }   // end of addFireStation


    public FireStation updateFireStation(FireStation fireStation) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<FireStation> fireStations = dataLoaded.getFirestations();

        // check if the fire station with input address exists and if so update the fire station number
        boolean fireStationExists = fireStations.stream()
                .anyMatch(firestation -> firestation.getAddress().equals(fireStation.getAddress()));

        if (fireStationExists) {
            FireStation fireStationToUpdate = fireStations.stream()
                    .filter(firestation -> firestation.getAddress().equals(fireStation.getAddress()))
                    .findFirst()
                    .orElse(null);
            fireStationToUpdate.setStation(fireStation.getStation());
            return fireStationToUpdate;
        }   else {
            throw new IllegalArgumentException("Fire station with address " + fireStation.getAddress() + " does not exist");
        }

        /*
        return fireStations.stream()
            .filter(f -> f.getAddress().equals(fireStation.getAddress()))
            .findFirst()
            .map(existingFireStation -> {
                existingFireStation.setStation(fireStation.getStation());
                return existingFireStation;
            })
            .orElseThrow(() -> new IllegalArgumentException("Fire station with address "
                    + fireStation.getAddress() + " does not exist"));
         */

    }   // end of updateFireStation

    public void deleteFireStation(FireStation fireStation) {
        // getting data from the service into DataLoaded POJO
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<FireStation> fireStations = dataLoaded.getFirestations();

      /*  boolean fireStationExists = fireStations.stream()
                .anyMatch(firestation -> firestation.getAddress().equals(fireStation.getAddress())
                        && firestation.getStation().equals(fireStation.getStation()));

        if (fireStationExists) {
            fireStations.remove(fireStation);
            dataLoaderService.saveData(dataLoaded);
        } else {
            throw new IllegalArgumentException("Fire station: " + fireStation.getStation() + " with address " + fireStation.getAddress() + " does not exist");
        }*/

        /*
        for(int i =0; i<fireStations.size(); i++) {
            FireStation currentFireStation = fireStations.get(i);
            if (currentFireStation.getAddress().equals(fireStation.getAddress())
                    && currentFireStation.getStation().equals(fireStation.getStation())) {
                fireStations.remove(i);
                dataLoaderService.saveData(dataLoaded);
                return;  // Exit after removing
            }
        } // end of for
        throw new IllegalArgumentException("Fire station: " + fireStation.getStation() + " with address " + fireStation.getAddress() + " does not exist");
        */
            boolean removed = fireStations.removeIf(firestation -> firestation.getAddress().equals(fireStation.getAddress())
                    && firestation.getStation().equals(fireStation.getStation()));  // remove the fire station

            if(removed) {
                dataLoaderService.saveData(dataLoaded);
            }
            else {
                throw new IllegalArgumentException("Fire station: " + fireStation.getStation() + " with address " + fireStation.getAddress() + " does not exist");
            }



    } // end of deleteFireStation

} // end of class
