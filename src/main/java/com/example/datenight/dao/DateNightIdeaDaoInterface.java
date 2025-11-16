package com.example.datenight.dao;

import com.example.datenight.model.DateNightIdea;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//An interface defines a contract: any class that implements it must provide
//implementations for all the methods inside. This allows you to separate the
//definition of data access operations from the implementation
public interface DateNightIdeaDaoInterface {
    //This method will find a random date night idea that matches a specific userId, budget category,
    //and categoryId (optionally). Optional indicates that it may or may not return data if all ideas have been used.
    Optional<DateNightIdea> findRandomAvailableIdea(String budgetCategory);

    //Create a date night idea
    DateNightIdea create(DateNightIdea idea);

    //Update an existing date night idea
    String update(int id, DateNightIdea idea);

    //The below deletes a date night idea by its id.
    boolean delete(int id);

    //Reset the list of ideas to make all ideas available again
    void resetListOfIdeas();

    //Get all date night ideas
    List<DateNightIdea> getAllIdeas();
}
