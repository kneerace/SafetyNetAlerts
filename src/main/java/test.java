//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.openclassrooms.SafetyNetAlerts.model.FireStation;
//
//import java.io.IOException;
//
//public class test {
//    public static void main(String[] args) {
//        System.out.println("test");
//
//        String data = "[\n" +
//                "    {\n" +
//                "      \"address\": \"1509 Culver St\",\n" +
//                "      \"station\": \"3\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"address\": \"29 15th St\",\n" +
//                "      \"station\": \"2\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"address\": \"834 Binoc Ave\",\n" +
//                "      \"station\": \"3\"\n" +
//                "    }\n" +
//                "]";
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            FireStation[] fireStations = mapper.readValue(data, FireStation[].class);
//            for (FireStation fireStation : fireStations) {
//                System.out.println(fireStation.getAddress());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//    } // end main
//}

//
//Nick Chang
//        08:03 AM
//        Can you hear me?
//        Nick Chang
//        08:12 AM
//        {"address": "123 st"}
//        Nick Chang
//        08:14 AM
//        {1: { [{"address": ... }, {"address": ... },...]}
//        Nick Chang
//        08:24 AM
//        package com.example.demo.controller;
//
//import com.example.demo.model.User;
//import com.example.demo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    @Autowired  // Injecting the service
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}")
//    public Optional<User> getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }
//}
