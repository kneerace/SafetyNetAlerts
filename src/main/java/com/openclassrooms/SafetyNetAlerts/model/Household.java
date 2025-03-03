package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

public class Household {

    private String address;
    private List<PersonDetails> personDetails;

    public Household(String address, List<PersonDetails> personDetails) {
        this.address = address;
        this.personDetails = personDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PersonDetails> getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(List<PersonDetails> personDetails) {
        this.personDetails = personDetails;
    }
}
