package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

public class FireStationServiceResponse {
    /*
    http://localhost:8080/firestation?stationNumber=<station_number>
    This URL should return a list of people serviced by the corresponding fire station. So if station number = 1,
    it should return the people serviced by station number 1. The list of people should include these specific pieces
    of information: first name, last name, address, phone number. As well, it should provide a summary of the number of
    adults in the service area and the number of children (anyone aged 18 or younger).
     */
    private List<PersonInfo> personServiced;
    private int numberOfAdults;
    private int numberOfChildren;

    public FireStationServiceResponse(List<PersonInfo> personServiced, int numberOfAdults, int numberOfChildren) {
        this.personServiced = personServiced;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public FireStationServiceResponse(){}

    public List<PersonInfo> getPersonServiced() {
        return personServiced;
    }

    public void setPersonServiced(List<PersonInfo> personServiced) {
        this.personServiced = personServiced;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
