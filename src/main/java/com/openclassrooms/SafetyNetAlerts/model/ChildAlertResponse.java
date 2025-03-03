package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

public class ChildAlertResponse {
    private String firstName;
    private String lastName;
    private int age;
    private List<Person> householdMembers;

    public ChildAlertResponse(String firstName, String lastName, int age, List<Person> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    } // end constructor

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHouseholdMembers(List<Person> householdMembers) {
        this.householdMembers = householdMembers;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getAge() {
        return age;
    }
    public List<Person> getHouseholdMembers() {
        return householdMembers;
    }


}
