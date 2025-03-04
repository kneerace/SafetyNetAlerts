package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.*;
import com.openclassrooms.SafetyNetAlerts.util.AgeCalculator;
import com.openclassrooms.SafetyNetAlerts.util.PersonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalService {
    private final LocalFileDataLoaderService dataLoaderService;
    @Autowired
    public PersonalService(LocalFileDataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

      /*
http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
This should return the person’s name, address, age, email, list of medications with dosages and allergies.
 If there is more than one person with the same name, this URL should return all of them.

*/

    public PersonalInfoResponse getPersonInfo( String firstName,  String lastName) {

//        logger.info("Request: /personInfo?firstname={}&lastname={}", firstName, lastName); // Log request

        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();

        List<Person> persons = dataLoaded.getPersons();
        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();

        Person person = persons.stream()
                .filter(person1 -> person1.getFirstName().equalsIgnoreCase(firstName) && person1.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (person == null) {
//            logger.info("No person found for first name: {} and last name: {}", firstName, lastName);
            return new PersonalInfoResponse();
        }

        MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
        PersonalInfoResponse response = new PersonalInfoResponse(person.getFirstName(), person.getLastName(), person.getAddress(),
                person.getEmail(),
                AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy"),
                medicalRecord.getMedications(), medicalRecord.getAllergies());

//        logger.info("Response: {}", response); //Make sure that PersonalInfoResponse has a toString method.

        return response;
    }

    /*
    http://localhost:8080/communityEmail?city=<city>
This will return the email addresses of all of the people in the city.
     */

    public List<String> getCommunityEmail(String city) {

//        logger.info("Request: /communityEmail?city={}", city); // Log request

        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();

        List<Person> persons = dataLoaded.getPersons();

        List<String> emailList = persons.stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());
        if(emailList.isEmpty()) {
//            logger.info("No email found for city: {}", city);  // log response
            return Collections.emptyList();
        }

//        logger.info("Emails for city: {} are: {}", city, emailList); // log response
        return emailList;
    } // end of getCommunityEmail

    public EmailListResponse getCommunityEmailList(String city) {

        List<String> emailList = dataLoaderService.getDataLoaded().getPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());

        return new EmailListResponse(emailList);
    } // end of getCommunityEmailList

    // Adding a new Person to tha Persons list
    public Person addPerson(Person person) {
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<Person> persons = dataLoaded.getPersons();
        persons.add(person);

        return person;
    } // end of addPerson

    /*
    Update an existing person (at this time, assume that firstName and lastName do not change, but other
    fields can be modified).
     */
    public Person updatePerson(Person person) {
        DataLoaded dataLoaded = dataLoaderService.getDataLoaded();
        List<Person> persons = dataLoaded.getPersons();
        //finding the index of person based on firstName and lastName match
        Optional<Person> existingPerson = persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && p.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst();

        if(existingPerson.isPresent()){
            Person foundPerson = existingPerson.get(); // extract the actual person as we can't compare person with optional person
            foundPerson.setAddress(person.getAddress());
            foundPerson.setCity(person.getCity());
            foundPerson.setZip(person.getZip());
            foundPerson.setPhone(person.getPhone());
            foundPerson.setEmail(person.getEmail());
            return foundPerson;
        }

        //        if(existingPerson.isPresent()){
//            Person foundPerson = existingPerson.get(); // extract the actual person as we can't compare person with optional person
//            int index = persons.indexOf(foundPerson);
//            persons.set(index, person);
//            return person;
//        }

//        else{
//            throw new IllegalArgumentException("Person not found in the list");
            return null;
//        }
    } // end of updatePerson


    /*
    Key Optional Methods
        Method	                Purpose
        isPresent() 	        Returns true if a value exists, false otherwise.
        get()	                Retrieves the value (throws exception if empty).
        orElse(defaultVal)	    Returns the value if present, otherwise returns defaultVal.
        orElseThrow()	        Throws an exception if the value is missing.
        ifPresent(Consumer<T>)	Runs a function if the value exists.
     */

} // end of class
