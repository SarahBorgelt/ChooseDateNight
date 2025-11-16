package com.example.datenight.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.time.LocalDateTime;

//Declare variables in the constructor
public class DateNightIdea {
    private Long id;
    private String title;
    private String description;
    private String budgetCategory;
    private Long categoryId;
    private String location;
    private LocalDateTime createdAt;
    private boolean isSuggested;

    //Generate getters and setters for each variable using encapsulation principles of Object-Oriented
    //Programming to ensure that private data may be accessed in another class.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudgetCategory() {
        return budgetCategory;
    }

    public void setBudgetCategory(String budgetCategory) {
        this.budgetCategory = budgetCategory;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSuggested() {
        return isSuggested;
    }

    public void setSuggested(boolean suggested) {
        isSuggested = suggested;
    }

}
