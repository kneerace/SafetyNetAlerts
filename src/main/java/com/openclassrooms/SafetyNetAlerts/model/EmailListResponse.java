package com.openclassrooms.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
public class EmailListResponse {

    private  List<String> emailList;

    public EmailListResponse(List<String> emailList) {
        this.emailList = emailList;
    }   // end of constructor

    public List<String> getEmailList() {
        return emailList;
    } // end of getEmailList

    //Ensure Jackson serializes an empty list as an empty JSON array "{}"
    @JsonInclude(JsonInclude.Include.NON_EMPTY)  // exclude empty list
    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    } // end of setEmailList

} // end of EmailListResponse
