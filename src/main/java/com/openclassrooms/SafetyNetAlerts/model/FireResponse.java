package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

public class FireResponse {
    private List<String> fireStaton; //
    private List<PersonDetails> personDetails;

    public FireResponse(List<String> fireStaton, List<PersonDetails> personDetails) {
        this.fireStaton = fireStaton;
        this.personDetails = personDetails;
    }

    public List<String> getFireStaton() {
        return fireStaton;
    }

    public void setFireStaton(List<String> fireStaton) {
        this.fireStaton = fireStaton;
    }

    public List<PersonDetails> getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(List<PersonDetails> personDetails) {
        this.personDetails = personDetails;
    }
}
