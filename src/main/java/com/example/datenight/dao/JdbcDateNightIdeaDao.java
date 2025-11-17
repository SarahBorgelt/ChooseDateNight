package com.example.datenight.dao;

import com.example.datenight.exception.DaoException;
import com.example.datenight.model.DateNightIdea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

//Component marks the class as a Spring-managed bean. Spring will automatically
//create an instance and allow it to be autowired into the service.
@Component
public class JdbcDateNightIdeaDao implements DateNightIdeaDaoInterface{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Logger log = LoggerFactory.getLogger(getClass());

    //Create a method to find a random idea available
    @Override
    public Optional<DateNightIdea> findRandomAvailableIdea(String budgetCategory) {
        DateNightIdea idea = null;
        String sql = "WITH next_idea AS(SELECT * FROM date_night_idea WHERE budget_category = ? AND is_suggested=FALSE ORDER BY RANDOM() LIMIT 1) " +
                "UPDATE date_night_idea SET is_suggested = TRUE WHERE id IN (SELECT id FROM next_idea) RETURNING *";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, budgetCategory);
            if(results.next()){
                idea = mapRowToIdea(results);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to the server or database", e);
        } return Optional.ofNullable(idea);
        //Optional.ofNullable contains value if it’s not null or
        //contains nothing (an empty Optional) if it is null
    }

    //Create a method to add to the date night idea database
    @Override
    public DateNightIdea create(DateNightIdea idea) {
        String sql= "INSERT INTO date_night_idea (title, description, budget_category, location)\n" +
                "VALUES(?,?,?,?) RETURNING *";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, idea.getTitle(), idea.getDescription(), idea.getBudgetCategory(), idea.getLocation());
            if(results.next()){
                return mapRowToIdea(results);
            } else {
                throw new DaoException("Failed to insert your date night idea into the database");
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to the server or database", e);
        }
    }

    //Create a method to update the date night idea database
    @Override
    public String update(int id, DateNightIdea idea) {
        String sql= "UPDATE date_night_idea SET title = ?, description = ?, budget_category = ?, location = ? WHERE id = ?";
        try{
            jdbcTemplate.update(sql, idea.getTitle(), idea.getDescription(), idea.getBudgetCategory(), idea.getLocation(), id);
            return "The date night idea has been successfully updated";
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to the server or database", e);
        }
    }

    //Create a method to delete an idea from the date night idea database
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM date_night_idea WHERE id = ?";
        try{
            int RowsAffected = jdbcTemplate.update(sql, id);
            return RowsAffected > 0;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to the server or database", e);
        }
    }

    //Create a method to reset the list of ideas to "has not been suggested"
    @Override
    public void resetListOfIdeas() {
        String sql = "UPDATE date_night_idea SET is_suggested=false";
        try{
            jdbcTemplate.update(sql);
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to the server or database", e);
        }
    }

    //Create a method to get all date night ideas
    @Override
    public List<DateNightIdea> getAllIdeas() {
        String sql = "SELECT * FROM date_night_idea";    
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            List<DateNightIdea> ideas = new java.util.ArrayList<>();
            while(results.next()){
                ideas.add(mapRowToIdea(results));
            }
            return ideas;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to the server or database", e);
        }
    }

    //Generate a helper method to connect rows to dateNightIdea object
    private DateNightIdea mapRowToIdea(SqlRowSet rs){
        DateNightIdea dateNightIdea = new DateNightIdea();
        dateNightIdea.setId(rs.getLong("id"));
        dateNightIdea.setTitle(rs.getString("title"));
        dateNightIdea.setDescription(rs.getString("description"));
        dateNightIdea.setBudgetCategory(rs.getString("budget_category"));
        dateNightIdea.setLocation(rs.getString("location"));
        //Ensure that if modified later to null, we do not call Timestamp.toLocalDateTime().
        //Timestamp.toLocalDateTime() cannot be called on null; it would throw a NullPointerException
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            dateNightIdea.setCreatedAt(createdAt.toLocalDateTime());
        }
        dateNightIdea.setSuggested(rs.getBoolean("is_suggested"));
        return dateNightIdea;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
