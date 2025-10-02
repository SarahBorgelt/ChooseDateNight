package com.example.datenight;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.stereotype.Service;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

@Service
public class DateNightService {
    //This points to the file name where user data is stored and loaded. By creating a constant.
    //we avoid hardcoding usersData.json everywhere
    private static final String DATA_FILE = "usersData.json";
    //The mapper is used to read/write the usersData.json file, turning the Java map
    //objects into JSON when saving and turning JSON back into Java objects when loading
    private final ObjectMapper mapper = new ObjectMapper();

    //Create hashmaps to store date night ideas categorized by budget and tracks which ideas a user has already used by budget.
    private Map<String, List<String>> dateNights = new HashMap<>();
    private Map<String, Map<String, List<String>>> usersData = new HashMap<>();


    public DateNightService() {
        loadUserData();

        //Create a resizeable ArrayList containing date night ideas by budget
        dateNights.put("Free", new ArrayList<>(Arrays.asList(
                "Board Game Night at Home",
                "Picnic in the Park",
                "Bike Riding",
                "Stargazing",
                "Columbus Commons Free Concert Night",
                "Columbus Commons Free Movie Series",
                "Columbus Museum of Art on Sunday (Free Admission)",
                "Riffe Art Gallery",
                "Columbus Metropolitan Library Culture Pass",
                "Franklin Park Conservatory First Sunday of the Month (Free Admission)",
                "Movie Marathon",
                "Volunteer Night Together",
                "Deeper Connections Cards",
                "Walk the Scioto Mile",
                "Visit the Park of Roses",
                "Free Tour of Columbus State House",
                "Innis Woods Metro Gardens",
                "Go on a Hike",
                "Play Disc Golf",
                "Shakespeare in the Park at Schiller Park",
                "Play Tennis at a Public Tennis Court"
        )));

        dateNights.put("Cheap", new ArrayList<>(Arrays.asList(
                "Sam's Club Cafe",
                "IKEA Cafe",
                "Farmer's Market",
                "DIY Art Night",
                "Visit a Coffee Shop",
                "DIY Spa Night",
                "Thrift Shopping",
                "Camping",
                "Factory Tour at Anthony Thomas Chocolates",
                "Grocery Store Challege (Each Make a Meal Under $10)",
                "Bubble Tea Tasting",
                "Triva Night at Pastimes Pub & Grill",
                "Olentangy Indian Caverns",
                "Visit a Corn Maze",
                "Paint-your-own-pottery at Clay Cafe or Color Me Mine",
                "Movie Matinee at Studio 35 or Gateway Film Center"
        )));

        dateNights.put("Moderate", new ArrayList<>(Arrays.asList(
                "Bowling",
                "Kayaking",
                "Ice Skating",
                "Mini Golf",
                "Axe Throwing",
                "Old North Arcade Bar",
                "Walk around German Village",
                "Go to a Columbus Clippers Game",
                "Swimming",
                "Visit Otherworld",
                "Go to BalletMet Columbus",
                "Eat at Mimi's Cafe",
                "Visit Ninja City",
                "Go Paddleboarding at Alum Creek",
                "Go to a Concert at Ace of Cups or Skully's"
        )));

        dateNights.put("Expensive", new ArrayList<>(Arrays.asList(
                "Top Golf",
                "Escape Room",
                "Murder Mystery Dinner",
                "Spa Day",
                "Helicopter Ride",
                "Boat Ride",
                "COSI",
                "See a Show at Ohio Theater",
                "Indoor Rock Climbing",
                "Dinner at The Refectory",
                "Segway Tour of the City",
                "Hot Air Balloon Ride",
                "Go Parasailng",
                "Go Jetskiing",
                "Go SCUBA Diving",
                "Go Skydiving",
                "Go Bungee Jumping",
                "Take a Skiing Lesson",
                "Take a Snowboarding Lesson",
                "Go Paragliding",
                "Go Ziplining",
                "Go to a Meditation Class",
                "Take a Ballroom Dancing Lesson",
                "Go to Laser Quest",
                "Go to a Columbus Blue Jackets Game",
                "Horseback Riding Trail Ride"
        )));
    }

    //Declare a save data method
    private void saveUserData(){
        try{
            //Save current user data to a JSON file in a readable format
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), usersData);
        } catch (IOException e){
            //If an error occurs, print it to the console
            e.printStackTrace();
        }
    }

    private void loadUserData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                //Load previous user data from usersData.json into memory when the application starts
                usersData = mapper.readValue(
                        file,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Map<String, List<String>>>>() {}
                );
            } catch (IOException e) {
                //If an error occurs, print it to the console
                e.printStackTrace();
            }
        }
    }

    public String getRandomDate(String userName, String budget){
        //Error handling based on each situation
        if(budget==null || budget.isBlank()){
            return "Budget cannot be empty. Please choose Free, Cheap, Moderate, or Expensive";
        }
        if(budget != null){
            budget = budget.substring(0,1).toUpperCase() + budget.substring(1).toLowerCase();
        }
        if(!dateNights.containsKey(budget)){
            return "Sorry, '" + budget + "' is not a valid category. Please choose Free, Cheap, Moderate, or Expensive";
        }

        //Initialize the data structures for a new user
        usersData.putIfAbsent(userName,new HashMap<>());
        Map<String, List<String>> userCategories = usersData.get(userName);
        userCategories.putIfAbsent(budget,new ArrayList<>());

        //Remove the ideas that have already been used and notify user if they have tried all ideas
        List<String> usedIdeas = userCategories.get(budget);
        List<String> availableIdeas = new ArrayList<>(dateNights.getOrDefault(budget, new ArrayList<>()));
        availableIdeas.removeAll(usedIdeas);
        if(availableIdeas.isEmpty()){
            return "Hi " + userName + "! You've tried all ideas in the " + budget + " category. Please reset to restart the list of ideas.";
        }
        //Pick a random idea
        String selectedIdea = availableIdeas.get(new Random().nextInt(availableIdeas.size()));
        usedIdeas.add(selectedIdea);
        saveUserData();
        return "Hi " + userName + "! Your " + budget + " date night idea is: " + selectedIdea;
}

    public String resetUser(String userName, String budget){
        if(!usersData.containsKey(userName)){
            return "No data to reset for this user.";
        }

        //Case 1: reset all if null/blank
        if(budget==null || budget.isBlank()){
            usersData.put(userName, new HashMap<>());
            saveUserData();
            return "All categories reset for " + userName;
        }

        //Normalize the budget for case handling
        budget = budget.substring(0,1).toUpperCase() + budget.substring(1).toLowerCase();

        //Validate the budget
        if(!dateNights.containsKey(budget)){
            return "Sorry, '" + budget + "' is not a valid category. Please enter Free, Cheap, Moderate, or Expensive";
        }

        //Reset only that category
        usersData.get(userName).put(budget, new ArrayList<>());
        saveUserData();
        return budget + " category reset for " + userName + ".";

    }
    }

