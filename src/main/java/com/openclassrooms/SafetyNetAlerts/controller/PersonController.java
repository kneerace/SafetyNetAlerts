package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // This means that this class is a Controller and is used where we the return is mapped to the view
@RequestMapping("/person")  // This means URL's start with /person (after Application path) i.e. basePath localhost:8080/person
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private PersonalService personalService;

    public PersonController(PersonalService personalService) {
        this.personalService = personalService;
    } // end constructor

    /*
    http://localhost:8080/person
This endpoint will provide the following via Http Post/Put/Delete:
Add a new person.
Update an existing person (at this time, assume that firstName and lastName do not change, but other
fields can be modified).
Delete a person. (Use a combination of firstName and lastName as a unique identifier)
     */

    @GetMapping
    @Profile("!prod") // this endpoint is only available in dev mode
    public ResponseEntity<?> getAllPersonRecords() {
        try{
            List<Person> persons = personalService.getAllPersons();
            return new ResponseEntity<>(persons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    } // end of getAllPerson

    @PostMapping  // adding a new person
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
        try {
            Person addedPerson = personalService.addPerson(person);
            // return added person with status created
//        return ResponseEntity.status(201).body(addedPerson);
            return new ResponseEntity<>(addedPerson, HttpStatus.CREATED);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", e.getMessage())
            );
        }

    } // end addPerson

    @PutMapping // updating existing person
    public ResponseEntity<?> updatePerson(@RequestBody Person person) {

        try{
                Person updatedPerson = personalService.updatePerson(person);
                return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch(IllegalArgumentException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", e.getMessage())
            );
        }
    } // end updatePerson


    @DeleteMapping
    public ResponseEntity<?> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        /*try{
            personalService.deletePerson(person);
            return  ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("message", "Person deleted successfully"));
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", e.getMessage())
            );
        }*/

        try{
            personalService.deletePerson(firstName, lastName);
            return ResponseEntity.status(HttpStatus.OK).body(
                    Map.of("message", "MedicalRecords for+" + firstName + " " + lastName + " deleted successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    } // end deletePerson
} // end class
