//package com.openclassrooms.SafetyNetAlerts.controller;
//
//import com.openclassrooms.SafetyNetAlerts.model.ChildAlertResponse;
//import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
//import com.openclassrooms.SafetyNetAlerts.model.MedicalRecord;
//import com.openclassrooms.SafetyNetAlerts.model.Person;
//import com.openclassrooms.SafetyNetAlerts.service.LocalFileDataLoaderService;
//import com.openclassrooms.SafetyNetAlerts.util.AgeCalculator;
//import com.openclassrooms.SafetyNetAlerts.util.PersonUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//public class ChildAlertController {
//
//    private static final Logger logger = LoggerFactory.getLogger(DataController.class);
//
//    private final LocalFileDataLoaderService dataLoaderService;
//
//    @Autowired
//    public ChildAlertController(LocalFileDataLoaderService dataLoaderService) {
//        this.dataLoaderService = dataLoaderService;
//    } // end of constructor
//     /*
//    http://localhost:8080/childAlert?address=<address>
//    This URL should return a list of children (anyone under the age of 18) at that address. The list should include the first
//    and last name of each child, the child’s age, and a list of other persons living at that address. If there are no children
//    at the address, then this URL can return an empty string.
//     */
//
//    @GetMapping("/childAlert")
//    public List<ChildAlertResponse> getChildByAddress(@RequestParam String address){
//        // getting data from the service into DataLoaded POJO
//        DataLoaded dataLoaded = dataLoaderService.loadData();
//
//        List<Person> persons = dataLoaded.getPersons();
//        List<MedicalRecord> medicalRecords = dataLoaded.getMedicalrecords();
//
//        List<Person> houseHoldMembers = persons.stream()
//                .filter(person -> person.getAddress().equalsIgnoreCase(address))
//                .toList();
//        logger.info("Total Household members: {}", houseHoldMembers.size());
//
//
//        // getting children i.e. anyone under the age of 18
//        List<ChildAlertResponse>  children = persons.stream()
//                .filter(person -> person.getAddress().equalsIgnoreCase(address) && PersonUtils.isChild(person, medicalRecords))
//                .map(person -> {
//                    MedicalRecord medicalRecord = PersonUtils.findMedicalRecord(person, medicalRecords);
//                    return new ChildAlertResponse(person.getFirstName(), person.getLastName(),
//                            AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy"), houseHoldMembers);
//                }).collect(Collectors.toList());
//        return children;
//    } // end of getChildByAddress
//
////    private MedicalRecord findMedicalRecord(Person person, List<MedicalRecord> medicalRecords) {
////        return medicalRecords.stream()
////                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
////                        && medicalRecord.getLastName().equalsIgnoreCase(person.getLastName()))
////                .findFirst()
////                .orElse(null);
////    } // end of findMedicalRecord
////    private boolean isChild(Person person, List<MedicalRecord> medicalRecords) {
////        return medicalRecords.stream()
////                .anyMatch(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName() )
////                        && medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())
////                        && AgeCalculator.calculateAge(medicalRecord.getBirthdate(), "MM/dd/yyyy") < 18);
////    } // end of isChild
//}
