package com.openclassrooms.SafetyNetAlerts.controller;

import com.openclassrooms.SafetyNetAlerts.model.Person;
import com.openclassrooms.SafetyNetAlerts.service.PersonalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping  // adding a new person
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {

        Person addedPerson = personalService.addPerson(person);
        // return added person with status created
//        return ResponseEntity.status(201).body(addedPerson);
         return new ResponseEntity<>(addedPerson, HttpStatus.CREATED);

    } // end addPerson

    @PutMapping // updating existing person
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        Person updatedPerson = personalService.updatePerson(person);

        if(updatedPerson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);


    } // end updatePerson

} // end class
