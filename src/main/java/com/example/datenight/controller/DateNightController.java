package com.example.datenight.controller;

import com.example.datenight.dao.DateNightIdeaDaoInterface;
import com.example.datenight.exception.DaoException;
import com.example.datenight.model.DateNightIdea;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.util.List;

//Using @CrossOrigins allows requests from the frontend running on any local host to access
//the backend without being blocked by CORS. CORS stands for cross-origin resource sharing and
//is a browser security feature that prevents a web page from talking to a different origin unless the
//server explicitly allows it.
@CrossOrigin(origins = "*")
@RestController //Tells spring this class handles HTTP requests and returns JSON responses
@RequestMapping("/api/date-night-ideas") //Sets the base URL for all endpoints in this controller
public class DateNightController {
    private DateNightIdeaDaoInterface dateNightIdeaDaoInterface;

    //DateNightIdeaInterface is injected into the controller. The controller calls service methods to handle
    //the actual logic (getting random dates, resetting user data, etc.)
    public DateNightController(DateNightIdeaDaoInterface dateNightIdeaDaoInterface){
        this.dateNightIdeaDaoInterface = dateNightIdeaDaoInterface;
    }

    //Get a random idea by budget category mapping
    @GetMapping("/random/{budgetCategory}") //Exposes this method as a GET endpoint
    public DateNightIdea getRandomIdea(@PathVariable String budgetCategory){
        return dateNightIdeaDaoInterface.findRandomAvailableIdea(budgetCategory)
                .orElseThrow(()->
                new ResponseStatusException(HttpStatus.NO_CONTENT, "No ideas found. Please reset the idea list"));
    }

    //Create reset ideas mapping
    @PostMapping("/reset") //exposes method as a POST endpoint
    public String resetideas(){
        try{
            dateNightIdeaDaoInterface.resetListOfIdeas();
            return "All ideas have been reset successfully";
        } catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to reset ideas", e);
        }
    }

    //Create add idea mapping
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addIdea")
    public DateNightIdea addDateNightIdea(@Valid @RequestBody DateNightIdea dateNightIdea){
        try{
            return dateNightIdeaDaoInterface.create(dateNightIdea);
        } catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add idea", e);
        }

    }

    //Create update idea mapping
    @PutMapping("/updateIdea/{id}")
    public String updateDateNightIdea(@PathVariable int id, @Valid @RequestBody DateNightIdea idea){
        try{
            dateNightIdeaDaoInterface.update(id, idea);
            return "Date night idea has been successfully updated";
        } catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update idea", e);
        }
    }

    //Create delete mapping
    @DeleteMapping("/deleteIdea/{id}")
    public String dateNightIdea(@PathVariable int id){
        try{
            dateNightIdeaDaoInterface.delete(id);
            return "Date night idea has been successfully deleted";
        } catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete idea", e);
        }
    }

    //Create get all ideas mapping
    @GetMapping("/allIdeas")
    public List<DateNightIdea> getAllIdeas(){
        return dateNightIdeaDaoInterface.getAllIdeas();
    }
}
