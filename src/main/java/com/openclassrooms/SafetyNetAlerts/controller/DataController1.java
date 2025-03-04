//package com.openclassrooms.SafetyNetAlerts.controller;
//
//import com.openclassrooms.SafetyNetAlerts.model.*;
//import com.openclassrooms.SafetyNetAlerts.service.LocalFileDataLoaderService;
//import com.openclassrooms.SafetyNetAlerts.util.AgeCalculator;
//import com.openclassrooms.SafetyNetAlerts.util.PersonUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//public class DataController1 {
//
//    private static final Logger logger = LoggerFactory.getLogger(DataController1.class);
//
//    private final LocalFileDataLoaderService dataLoaderService;
//
//    @Autowired
//    public DataController1(LocalFileDataLoaderService dataLoaderService) {
//        this.dataLoaderService = dataLoaderService;
//    } // end of constructor
//
//
//    /*
//    http://localhost:8080/firestation?stationNumber=<station_number>
//    This URL should return a list of people serviced by the corresponding fire station. So if station number = 1,
//    it should return the people serviced by station number 1. The list of people should include these specific pieces
//    of information: first name, last name, address, phone number. As well, it should provide a summary of the number of
//    adults in the service area and the number of children (anyone aged 18 or younger).
//     */
//    @GetMapping("/firestation")
//    public FireStationServiceResponse getFireStationByStationNumber(@RequestParam int stationNumber) {
//        // getting data from the service into DataLoaded POJO
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
//        List<FireStation> fireStations = dataLoaded.getFirestations(); // getting fire stations
//
//        // list address served by fire station
//        List<String> addressServed = fireStations.stream()
//                .filter(firestation -> firestation.getStation().equals(String.valueOf(stationNumber)))
//                .map(FireStation::getAddress)
//                .collect(Collectors.toList());
//
////        if(addressServed.isEmpty()) {
////            logger.info("No fire station found for station number: {}", stationNumber);  // log response
////            return new FireStationServiceResponse();
////        }
//
//        // get Persons based on the address collected above
//        List<Person> personsServed = persons.stream()
//                .filter(person -> addressServed.contains(person.getAddress()))
//                .collect(Collectors.toList());
//
//        // Mapping persons to PersonInfo for response
//        List<PersonInfo> personInfos = personsServed.stream()
//                .map(person -> new PersonInfo(
//                        person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()
//                )).collect(Collectors.toList());
//
//
//        //getting adult and childred ( anyone aged 18 or younger
//        int numberOfAdults = 0;
//        int numberOfChildren = 0;
//        for (Person person : personsServed) {
//            MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
//
//            if (medicalRecord != null) {
//                if (AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy") > 18) {
//                    numberOfAdults++;
//                } else {
//                    numberOfChildren++;
//                }
//            }
//        }  // end for
//        return new FireStationServiceResponse(personInfos, numberOfAdults, numberOfChildren);
//    } // end of getFireStationByStationNumber
//
//
//    /*
//    http://localhost:8080/phoneAlert?firestation=<firestation_number>
//    This URL should return a list of phone numbers of each person within the fire station’s jurisdiction.
//    We’ll use this to send emergency text messages to specific households.
//    */
//    @GetMapping("/phoneAlert")
//    public List<String> getPhoneNumbersWithinFireStationJurisdiction(@RequestParam int firestation) {
//        // getting data from the service into DataLoaded POJO
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//        List<FireStation> fireStations = dataLoaded.getFirestations(); // getting fire stations
//
////        getting Persons based on the address reflected in the fire station
//        List<Person> personsServed = persons.stream()
//                .filter(person -> fireStations.stream()
//                        .filter(firestation1 -> firestation1.getStation().equals(String.valueOf(firestation)))
//                        .map(FireStation::getAddress)
//                        .collect(Collectors.toList())
//                        .contains(person.getAddress()))
//                .collect(Collectors.toList());
//
//        List<String> phoneNumbers = personsServed.stream()
//                .map(Person::getPhone)
//                .collect(Collectors.toList());
//        return phoneNumbers;
//
//    } // end of getPhoneNumbersWithinFireStationJurisdiction
//
//    /*http://localhost:8080/fire?address=<address>
//    This URL should return the fire station number that services the provided address as well as a list
//    of all of the people living at the address. This list should include each person’s name, phone number,
//     age, medications with dosage, and allergies.
//    */
//
//    @GetMapping("/fire")
//    public FireResponse getFireStationByAddress(@RequestParam String address) {
//        // getting data from the service into DataLoaded POJO
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
//        List<FireStation> fireStations = dataLoaded.getFirestations(); // getting fire stations
//
//        // getting fire station by address
//        List<String> fireStationsNumber = fireStations.stream()
//                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address))
//                .map(FireStation::getStation)
//                .collect(Collectors.toList());
//
//        // getting Persons based on the address, with medical and allergies
//        List<PersonDetails> personDetails = persons.stream()
//                .filter(person -> person.getAddress().equalsIgnoreCase(address))
//                .map(person -> {
//                    MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
//                    int age = AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy");
//                    return new PersonDetails(
//                                person.getFirstName(),
//                                person.getLastName(),
//                                person.getPhone(),
//                                age,
//                                medicalRecord.getMedications(),
//                                medicalRecord.getAllergies());
//                })
//                .collect(Collectors.toList());
//
//        //return the response
//        return new FireResponse(fireStationsNumber, personDetails);
//
//    } // end of getFireStationByAddress
//
//    /*
//    http://localhost:8080/flood/stations?stations=<a list of station_numbers>
//    http://localhost:8080/flood/stations?stations=1,2
//    This should return a list of all the households in each fire station’s jurisdiction.
// This list needs to group people by household address, include name, phone number, and age of each person,
//  and any medications (with dosages) and allergies beside each person’s name.
//
//  {
//    "stationNumber": "1",
//    "households": [
//
//    ]
//
//
//*/
//    @GetMapping("/flood/stations")
//    public List<HouseholdByStation> getHouseholdByFireStations(@RequestParam List<String> fireStations) {
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
//        List<FireStation> fireStationsList = dataLoaded.getFirestations();
//
////         response to store the response while iterating
//        List<HouseholdByStation> response = new ArrayList<>();
//
//        //iterate through the list of fire stations
//        for (String fireStation : fireStations) {  // fireStations = [1,3]
//            // getting fire station by address
//            List<FireStation> filteredFireStation = fireStationsList.stream()
//                    .filter(filterFirestation -> filterFirestation.getStation().equals(fireStation))
//                    .collect(Collectors.toList());
//
//            // handle if filteredFireStation is empty
//            if (filteredFireStation.isEmpty()) {
//                logger.info("No fire station found for fire station number: {}" , fireStation);
//                continue;  // skip to the next station
//            } // end if
//
//            Map<String, List<PersonDetails>> households = new HashMap<>();
//
//            // iterating through filtered firestation for person and address
//            for(FireStation firestation : filteredFireStation) {
//                String addressCovered = firestation.getAddress();
//
//                List<PersonDetails> personDetails = persons.stream()
//                        .filter(person -> person.getAddress().equalsIgnoreCase(addressCovered))
//                        .map(person -> {
//                            MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
//                            int age = AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy");
//                            return new PersonDetails(
//                                    person.getFirstName(),
//                                    person.getLastName(),
//                                    person.getPhone(),
//                                    age,
//                                    medicalRecord.getMedications(),
//                                    medicalRecord.getAllergies());
//                        })
//                        .collect(Collectors.toList());
//
//
//
//                households.put(addressCovered, personDetails);
//
//
//            } // end for
//
//            List<Household> householdList = households.entrySet().stream()
//                    .map(entry -> new Household(entry.getKey(), entry.getValue()))
//                    .collect(Collectors.toList());
//            // adding to the response
//            response.add(new HouseholdByStation(fireStation, householdList));
//
//        } // end for
//
//        return response;
//
//    } // end of getHouseholdByFireStations
//
//    /*
//http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
//This should return the person’s name, address, age, email, list of medications with dosages and allergies.
// If there is more than one person with the same name, this URL should return all of them.
//
//*/
//
//    @GetMapping("/personInfo")
//    public PersonalInfoResponse getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
//
//        logger.info("Request: /personInfo?firstname={}&lastname={}", firstName, lastName); // Log request
//
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
//
//        Person person = persons.stream()
//                .filter(person1 -> person1.getFirstName().equalsIgnoreCase(firstName) && person1.getLastName().equalsIgnoreCase(lastName))
//                .findFirst()
//                .orElse(null);
//
//        if (person == null) {
//            logger.info("No person found for first name: {} and last name: {}", firstName, lastName);
//            return new PersonalInfoResponse();
//        }
//
//        MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
//        PersonalInfoResponse response = new PersonalInfoResponse(person.getFirstName(), person.getLastName(), person.getAddress(),
//                person.getEmail(),
//                AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy"),
//                medicalRecord.getMedications(), medicalRecord.getAllergies());
//
//        logger.info("Response: {}", response); //Make sure that PersonalInfoResponse has a toString method.
//
//        return response;
//    }
//
//    /*
//    http://localhost:8080/communityEmail?city=<city>
//This will return the email addresses of all of the people in the city.
//     */
//
//    @GetMapping("/communityEmail")
//    public String getCommunityEmail(@RequestParam String city) {
//
//        logger.info("Request: /communityEmail?city={}", city); // Log request
//
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//
//        String emailList = persons.stream()
//                .filter(person -> person.getCity().equalsIgnoreCase(city))
//                .map(Person::getEmail)
//                .collect(Collectors.joining(", "));
//        if(emailList.isEmpty()) {
//            logger.info("No email found for city: {}", city);  // log response
//            return "{}";
//        }
//
//        logger.info("Emails for city: {} are: {}", city, emailList); // log response
//        return emailList;
//    } // end of getCommunityEmail
//
//} // end of class
