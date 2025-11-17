package com.example.datenight;

import com.example.datenight.controller.DateNightController;
import com.example.datenight.dao.DateNightIdeaDaoInterface;
import com.example.datenight.exception.DaoException;
import com.example.datenight.model.DateNightIdea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DateNightControllerTest {
    //Variable initialization
    private DateNightIdeaDaoInterface daoMock;
    private DateNightController controller;

    // Before each test, create a mock DAO and inject it into the controller
    @BeforeEach
    void setUp() {
        daoMock = Mockito.mock(DateNightIdeaDaoInterface.class);
        controller = new DateNightController(daoMock);
    }

    // Tests that the controller returns the idea provided by the DAO for a given budget category
    // This does NOT test randomness; it only verifies that the controller correctly handles a DAO response
    @Test
    void getRandomIdea_ReturnsIdea_WhenFound() {
        DateNightIdea idea = new DateNightIdea();
        idea.setId(1L);
        idea.setTitle("Movie Night");
        when(daoMock.findRandomAvailableIdea("Free")).thenReturn(Optional.of(idea));

        DateNightIdea result = controller.getRandomIdea("Free");

        assertEquals("Movie Night", result.getTitle());
        verify(daoMock, times(1)).findRandomAvailableIdea("Free");
    }

    // Tests that the controller throws a NO_CONTENT (204) exception when the DAO returns no idea
    // Verifies that the controller correctly handles the empty Optional scenario
    @Test
    void getRandomIdea_ThrowsNoContent_WhenNotFound() {
        when(daoMock.findRandomAvailableIdea("Free")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getRandomIdea("Free");
        });

        assertEquals(HttpStatus.NO_CONTENT, exception.getStatusCode());
    }

    // Tests that the controller successfully resets all ideas when the DAO does not throw an exception
    @Test
    void resetIdeas_ReturnsSuccessMessage_WhenNoException() throws DaoException {
        doNothing().when(daoMock).resetListOfIdeas();

        String result = controller.resetideas();

        assertEquals("All ideas have been reset successfully", result);
        verify(daoMock, times(1)).resetListOfIdeas();
    }

    // Tests that the controller throws INTERNAL_SERVER_ERROR when the DAO fails to reset ideas
    @Test
    void resetIdeas_ThrowsInternalServerError_WhenDaoException() throws DaoException {
        doThrow(new DaoException("DB error")).when(daoMock).resetListOfIdeas();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.resetideas();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    // Tests that the controller successfully adds a new date night idea and returns it
    @Test
    void addDateNightIdea_ReturnsCreatedIdea_WhenSuccessful() throws DaoException {
        DateNightIdea idea = new DateNightIdea();
        idea.setTitle("Dinner Date");

        when(daoMock.create(idea)).thenReturn(idea);

        DateNightIdea result = controller.addDateNightIdea(idea);

        assertEquals("Dinner Date", result.getTitle());
        verify(daoMock, times(1)).create(idea);
    }

    // Tests that the controller throws INTERNAL_SERVER_ERROR when the DAO fails to create a new idea
    @Test
    void addDateNightIdea_ThrowsInternalServerError_WhenDaoException() throws DaoException {
        DateNightIdea idea = new DateNightIdea();
        idea.setTitle("Dinner Date");

        when(daoMock.create(idea)).thenThrow(new DaoException("DB error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.addDateNightIdea(idea);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    // Tests that the controller throws INTERNAL_SERVER_ERROR when updating an idea fails in the DAO
    @Test
    void updateDateNightIdea_ThrowsInternalServerError_WhenDaoException() throws DaoException {
        DateNightIdea idea = new DateNightIdea();
        idea.setTitle("Picnic");

        doThrow(new DaoException("DB error")).when(daoMock).update(1, idea);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.updateDateNightIdea(1, idea);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    // Tests that the controller throws INTERNAL_SERVER_ERROR when deleting an idea fails in the DAO
    @Test
    void dateNightIdea_ThrowsInternalServerError_WhenDaoException() throws DaoException {
        doThrow(new DaoException("DB error")).when(daoMock).delete(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.dateNightIdea(1);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    // Tests that the controller returns the full list of ideas provided by the DAO
    @Test
    void getAllIdeas_ReturnsListOfIdeas() {
        DateNightIdea idea1 = new DateNightIdea();
        idea1.setTitle("Movie Night");

        DateNightIdea idea2 = new DateNightIdea();
        idea2.setTitle("Dinner");

        when(daoMock.getAllIdeas()).thenReturn(List.of(idea1, idea2));

        List<DateNightIdea> result = controller.getAllIdeas();

        assertEquals(2, result.size());
        verify(daoMock, times(1)).getAllIdeas();
    }
}
