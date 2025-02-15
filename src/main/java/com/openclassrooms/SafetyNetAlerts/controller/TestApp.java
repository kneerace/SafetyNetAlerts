package com.openclassrooms.SafetyNetAlerts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller // This means that this class is a Controller and is used where we the return is
    // mapped to the view
@RestController // typically used when you're creating a RESTful API where the responses are u
    // sually JSON, XML, or plain text. It combines @Controller and @ResponseBody, meaning the
    // returned values are written directly to the HTTP response body instead of being mapped to a view. Ideal for backend services without a view layer.

public class TestApp {

    @GetMapping("/message")
    public String checkMessage() {
        return "Hi Just a test";
    } // end checkMessage

    @GetMapping
    public String homePage() {
        return "Hi we are in HomePage now ";
    } // end checkMessage

} // end TestApp
