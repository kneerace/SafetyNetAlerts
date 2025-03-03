package com.openclassrooms.SafetyNetAlerts.model;

import java.util.List;

public class HouseholdByStation {

    private String fireStation;
    private List<Household> households;

    public HouseholdByStation(String fireStation, List<Household> households) {
        this.fireStation = fireStation;
        this.households = households;
    } // end constructor

    public String getFireStation() {
        return fireStation;
    }

    public void setFireStation(String fireStation) {
        this.fireStation = fireStation;
    }

    public List<Household> getHouseholds() {
        return households;
    }

    public void setHouseholds(List<Household> households) {
        this.households = households;
    }
}
