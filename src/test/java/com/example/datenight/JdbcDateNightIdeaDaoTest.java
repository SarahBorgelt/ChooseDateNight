package com.example.datenight;

import com.example.datenight.dao.JdbcDateNightIdeaDao;
import com.example.datenight.exception.DaoException;
import com.example.datenight.model.DateNightIdea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JdbcDateNightIdeaDaoTest {
    private JdbcTemplate jdbcTemplateMock;
    private JdbcDateNightIdeaDao dao;

    @BeforeEach
    void setUp() {
        // Before each test, create a mock JdbcTemplate and inject it into the DAO
        jdbcTemplateMock = mock(JdbcTemplate.class);
        dao = new JdbcDateNightIdeaDao();
        dao.setJdbcTemplate(jdbcTemplateMock);
    }

    // Tests that findRandomAvailableIdea returns a DateNightIdea object when a row exists
    // Verifies mapping from SqlRowSet to DateNightIdea, using budget category "Free"
    @Test
    void findRandomAvailableIdea_ReturnsIdea_WhenFound() throws DaoException {
        SqlRowSet rs = mock(SqlRowSet.class);
        when(jdbcTemplateMock.queryForRowSet(anyString(), eq("Free"))).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("title")).thenReturn("Movie Night");
        when(rs.getString("description")).thenReturn("Watch a movie at home");
        when(rs.getString("budget_category")).thenReturn("Free");
        when(rs.getString("location")).thenReturn("Home");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(rs.getBoolean("is_suggested")).thenReturn(false);

        Optional<DateNightIdea> result = dao.findRandomAvailableIdea("Free");

        assertTrue(result.isPresent());
        assertEquals("Movie Night", result.get().getTitle());
    }

    // Tests that findRandomAvailableIdea returns an empty Optional if no rows exist
    // Simulates no available ideas for budget category "Cheap"
    @Test
    void findRandomAvailableIdea_ReturnsEmpty_WhenNoResults() {
        SqlRowSet rs = mock(SqlRowSet.class);
        when(jdbcTemplateMock.queryForRowSet(anyString(), eq("Cheap"))).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Optional<DateNightIdea> result = dao.findRandomAvailableIdea("Cheap");

        assertTrue(result.isEmpty());
    }

    // Tests that create successfully inserts a new idea and maps the returned row
    // Verifies mapping and fields, using budget category "Moderate"
    @Test
    void create_ReturnsCreatedIdea_WhenSuccessful() throws DaoException {
        SqlRowSet rs = mock(SqlRowSet.class);
        DateNightIdea idea = new DateNightIdea();
        idea.setTitle("Dinner Date");
        idea.setDescription("Dinner at a restaurant");
        idea.setBudgetCategory("Moderate");
        idea.setLocation("Downtown");

        when(jdbcTemplateMock.queryForRowSet(anyString(), any(), any(), any(), any())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("title")).thenReturn("Dinner Date");
        when(rs.getString("description")).thenReturn("Dinner at a restaurant");
        when(rs.getString("budget_category")).thenReturn("Moderate");
        when(rs.getString("location")).thenReturn("Downtown");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(rs.getBoolean("is_suggested")).thenReturn(false);

        DateNightIdea result = dao.create(idea);

        assertEquals("Dinner Date", result.getTitle());
    }

    // Tests that create throws a DaoException if the insert returns no row
    // Simulates a failure in inserting a new idea for budget category "Expensive"
    @Test
    void create_ThrowsDaoException_WhenNoRowReturned() {
        SqlRowSet rs = mock(SqlRowSet.class);
        when(jdbcTemplateMock.queryForRowSet(anyString(), any(), any(), any(), any())).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        DateNightIdea idea = new DateNightIdea();
        idea.setTitle("Luxury Dinner");
        idea.setBudgetCategory("Expensive");

        DaoException exception = assertThrows(DaoException.class, () -> dao.create(idea));
        assertTrue(exception.getMessage().contains("Failed to insert"));
    }

    // Tests that delete returns true if a row is deleted
    // Simulates deleting an idea with budget category "Cheap"
    @Test
    void delete_ReturnsTrue_WhenRowDeleted() {
        when(jdbcTemplateMock.update(anyString(), eq(1))).thenReturn(1);

        boolean result = dao.delete(1);
        assertTrue(result);
    }

    // Tests that delete returns false if no rows are deleted
    // Simulates deleting a non-existent idea for budget category "Moderate"
    @Test
    void delete_ReturnsFalse_WhenNoRowDeleted() {
        when(jdbcTemplateMock.update(anyString(), eq(1))).thenReturn(0);

        boolean result = dao.delete(1);
        assertFalse(result);
    }

    // Tests that getAllIdeas returns a list of DateNightIdea objects correctly mapped
    // Verifies mapping for a single idea with budget category "Free"
    @Test
    void getAllIdeas_ReturnsMappedIdeas() {
        SqlRowSet rs = mock(SqlRowSet.class);
        when(jdbcTemplateMock.queryForRowSet(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); // one row
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("title")).thenReturn("Movie Night");
        when(rs.getString("description")).thenReturn("Watch a movie");
        when(rs.getString("budget_category")).thenReturn("Free");
        when(rs.getString("location")).thenReturn("Home");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(rs.getBoolean("is_suggested")).thenReturn(false);

        List<DateNightIdea> result = dao.getAllIdeas();

        assertEquals(1, result.size());
        assertEquals("Movie Night", result.get(0).getTitle());
    }
}
