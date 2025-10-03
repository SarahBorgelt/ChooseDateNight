package com.example.datenight;

import com.example.datenight.DateNightService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateNightServiceTest {
    private DateNightService sut;

    @BeforeEach
    public void setup(){
        sut = new DateNightService();
    }

    @Test
    public void dateNightServiceHappyPath0(){
        //Arrange

        //Act
        String result = sut.getRandomDate("Alice", "Free");

        //Assert
        assertTrue(result.contains("Hi Alice! Your Free date night idea is:"));
    }

    @Test
    public void dateNightServiceHappyPath1(){
        //Arrange

        //Act
        String result = sut.getRandomDate("Johnny", "Expensive");

        //Assert
        assertTrue(result.contains("Hi Johnny! Your Expensive date night idea is:"));
    }

    @Test
    public void dateNightServiceHappyPath3(){
        //Arrange

        //Act
        String result = sut.getRandomDate("Sally", "Moderate");

        //Assert
        assertTrue(result.contains("Hi Sally! Your Moderate date night idea is:"));
    }

    @Test
    public void dateNightServiceEdgeTesting0(){
        //Arrange

        //Act
        String result = sut.getRandomDate("Jay", "");

        //Assert
        assertTrue(result.contains("Budget cannot be empty"));
    }

    @Test
    public void dateNightServiceEdgeTesting1(){
        //Arrange

        //Act
        String result = sut.getRandomDate("Jay", "No");

        //Assert
        assertTrue(result.contains("Sorry, 'No' is not a valid category"));
    }

    @Test
    public void dateNightServiceEdgeTesting2() {
        // Act
        String result = sut.getRandomDate(null, "");

        // Assert
        assertTrue(result.contains("Budget cannot be empty"),
                "Expected error message for empty budget not returned");
    }

    @Test
    public void dateNightServiceEdgeTesting3() {
        // Act
        String result = sut.getRandomDate("BLAKE", "CHEAP");

        // Assert
        assertTrue(result.contains("Hi BLAKE! Your Cheap date night idea is:"));

    }

    @Test
    public void resetUser_SingleCategory() {
        //Arrange
        sut.getRandomDate("Alice", "Free");
        //Act
        String result = sut.resetUser("Alice", "Free");
        //Assert
        assertTrue(result.contains("Free category reset for Alice"));
    }

    @Test
    public void resetUser_AllCategories() {
        sut.getRandomDate("Alice", "Cheap");
        //Act
        String result = sut.resetUser("Alice", null);
        //Assert
        assertTrue(result.contains("All categories reset for Alice"));
    }

    @Test
    public void resetUser_NonExistentUser() {
        //Act
        String result = sut.resetUser("Ghost", "Free");
        //Assert
        assertTrue(result.contains("No data to reset for this user."));
    }
}
