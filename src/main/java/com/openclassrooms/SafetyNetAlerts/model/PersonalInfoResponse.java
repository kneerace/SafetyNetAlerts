package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

public class PersonalInfoResponse {
//    This should return the person’s name, address, age, email, list of medications with dosages and allergies.
//    If there is more than one person with the same name, this URL should return all of them.

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private int age;
    private List<String> medicalRecords;
    private List<String> allergies;

    public PersonalInfoResponse(String firstName, String lastName, String address, String email, int age, List<String> medicalRecords, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.age = age;
        this.medicalRecords = medicalRecords;
        this.allergies = allergies;
    }

    public PersonalInfoResponse() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<String> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
