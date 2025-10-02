package com.example.datenight;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController //Tells spring this class handles HTTP requests and returns JSON responses
@RequestMapping("/api") //Sets the base URL for all endpoints in this controller
public class DateNightController {
    private final DateNightService dateNightService;

    //DateNightService is injected into the controller. The controller calls service methods to handle
    //the actual logic (getting random dates, resetting user data, etc.)
    public DateNightController(DateNightService dateNightService){
        this.dateNightService = dateNightService;
    }

    @GetMapping("/date-night") //Exposes this method as a GET endpoint
    public ResponseEntity<Map<String, String>> getDateNight(@RequestParam String name, @RequestParam String budget){
        //Gets query parameters from the URL
        String idea = dateNightService.getRandomDate(name, budget);
        //Calls the service to fetch a random date idea for that user and budget
        return ResponseEntity.ok(Map.of(
                "user", name,
                "budget", budget,
                "idea", idea
        )); //Returns HTTP 200 OK with JSON body
    }

    @PostMapping("/reset") //exposes method as a POST endpoint
    public ResponseEntity<Map<String, String>> reset(@RequestParam String name, @RequestParam(required = false) String budget){
        //This makes the budget parameter optional. If a budget is not provided, all categories for the user are reset.
        String message = dateNightService.resetUser(name, budget);
        //Calls the service to reset the user's used date ideas for either a specific category or all categories
        return ResponseEntity.ok(Map.of(
                "user", name,
                "budget", budget != null ? budget : "All",
                "message", message
        )); //Returns HTTP 200 OK with JSON
    }
}
