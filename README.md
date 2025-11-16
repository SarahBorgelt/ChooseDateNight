# Date Night Ideas App

**A full-stack web application for discovering, managing, and tracking creative date night ideas.**  
Users can generate random ideas by budget, add new ideas, edit or delete existing ones, and reset the list of suggestions.

![Screenshot of Date Night Ideas App](DateNightScreenshot.png "App Screenshot")

---

## Table of Contents

- [Project Overview](#project-overview)
- [Why This Project Matters](#why-this-project-matters)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [Backend API](#backend-api)
- [Setup Instructions](#setup-instructions)

---

## Project Overview

This application demonstrates a complete full-stack solution, combining a Spring Boot backend with a JavaScript frontend. It emphasizes:

- RESTful API design
- Robust CRUD operations with a relational database
- Error handling using a custom `DaoException` with inheritance from `RuntimeException`
- Responsive, interactive UI with modal forms

---

## Why This Project Matters

Maintaining meaningful connections often requires creativity and planning. This project provides a practical, easy-to-use tool for couples:

- **Encourage quality time:** Inspires unique, budget-conscious date ideas to help you create memorable experiences.

- **implify planning:** Eliminates the stress of deciding what to do, making it easy to pick something fun and suitable for your budget.

- **Bring variety to your routine:** Offers new and unexpected ideas to keep outings exciting and engaging.

By focusing on user experience, this app goes beyond planning—it helps people connect, explore, and enjoy meaningful moments together.

## Key Features

- **Random Idea Generator:** Select a budget and receive a random date night idea.
- **Add / Update / Delete Ideas:** Full CRUD support for managing ideas.
- **Reset Suggestions:** Reset all ideas to an unsuggested state with a success message.
- **Persistent Storage:** PostgreSQL database stores all ideas with status tracking.
- **Responsive UI:** Clean, CSS-styled interface with modal forms for add/update operations.

---

## Technology Stack

- **Backend:** Java, Spring Boot, JDBC
- **Database:** PostgreSQL
- **Frontend:** HTML5, CSS3, JavaScript (ES6)
- **Build Tool:** Maven
- **Error Handling:** Custom `DaoException` for consistent backend error management

---

## Backend API

Base URL: `http://localhost:9090/api/date-night-ideas`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/random/{budgetCategory}` | Returns a random idea for the specified budget (`Free`, `Cheap`, `Moderate`, `Expensive`). |
| POST   | `/reset` | Resets all ideas to an unsuggested state. Returns a success message. |
| POST   | `/addIdea` | Adds a new date night idea. Requires JSON body with `title`, `description`, `budgetCategory`, `location`. |
| PUT    | `/updateIdea/{id}` | Updates an existing idea by ID. Requires JSON body with updated fields. |
| DELETE | `/deleteIdea/{id}` | Deletes a date night idea by ID. |
| GET    | `/allIdeas` | Retrieves all date night ideas. |

**Example JSON payload for add/update:**

```json
{
    "title": "Picnic in the Park",
    "description": "Pack sandwiches and enjoy a sunny day outdoors",
    "budgetCategory": "Free",
    "location": "Local park"
}
```

## Setup Instructions

### 1. Clone the repository
    git clone <https://github.com/SarahBorgelt/ChooseDateNight.git>

### 2. Configure PostgreSQL
In the application.properties (src/main/resources/application.properties) file,
update the datasource username and password to your PostgreSQL credentials.

### 3. Create the database
In PostgreSQL, create a database entitled "DateNight". Once created, right-click on it
and select "Query Tool". Click on the folder icon to open a folder and navigate to the
"DateNight.sql" file in the root directory. Open the "DateNight.sql" file and execute
the script to create the necessary columns.

- *It should be noted that if you already have a table entitled "date_night_idea",
that table will be deleted and a new one will be created. Proceed with caution.*

### 4. Start the application
- **macOS/Linux:** Double-click the start-mac-or-linux.command file in the project folder to launch the app in a terminal.

- **Windows:** Double-click the start-windows.bat file to launch the app.

### 5. The app will start on http://localhost:9090