# DateNightApp

**DateNightApp** is a Spring Boot application that generates random date night ideas in the Columbus, Ohio area based on a user's budget, while tracking previous ideas to avoid repetition. It is designed with JSON APIs for easy integration with frontend applications.

---

## Features

- Random date night suggestions across four budget categories: **Free, Cheap, Moderate, Expensive**
- Prevents repeating suggestions until reset
- Reset options for individual budget categories or all categories
- JSON-based API responses for frontend use
- Persistent user data saved in `usersData.json`

---

## Technology Stack

- **Java 25**
- **Spring Boot 3.3**
- **Jackson** for JSON serialization
- **Maven** for project management
- Embedded **Tomcat server**

---

## Installation & Running

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd DateNightApp
2. Build and run:

    ```bash
    Run mvn spring-boot:run in your terminal

3. Build and run:
    ```
    Application will start at http://localhost:9090

# API Endpoints
## Get Random Date Night
### GET /api/date-night
#### Parameters:

    name (string, required) – User's name
    
    budget (string, required) – Budget category (Free, Cheap, Moderate, Expensive)

#### Response Example:


    {
    "user": "Sarah",
    "budget": "Free",
    "idea": "Picnic in the Park"
    }

### Reset User Data
#### POST /api/reset
##### Parameters:

    name (string, required) – User's name
    
    budget (string, optional) – Category to reset; leave blank to reset all

#### Response Example:
    {
    "user": "Sarah",
    "budget": "All",
    "message": "All categories reset for Sarah"
    }

### Data Persistence
- User data is stored in usersData.json
- Tracks which ideas each user has used
- Persists across server restarts

### Usage Examples
Get a free date night idea by replacing YourName with your name and amountToSpend with one of these options: Free, Cheap, Moderate, Expensive.

    GET http://localhost:9090/api/date-night?name=YourName&budget=amountToSpend

##### Reset a single category:
To reset the program after you run out of a category, replace YourName with your name and amountToSpend with one of these options: Free, Cheap, Moderate, Expensive.

    POST http://localhost:9090/api/reset?name=YourName&budget=amountToSpend

#### Reset all categories:
To reset all categories, replace YourName with your name

    POST http://localhost:9090/api/reset?name=YourName