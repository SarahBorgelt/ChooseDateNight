-- ==========================================
-- Date Night App Database Setup (Flexible Categories)
-- ==========================================
-- To create the correct database, first create a database in PostgreSQL named "DateNight".
-- Then run this script to create the necessary tables.
-- ==========================================

-- Drop tables if they already exist (Safe to re-run. Previous tables under these names will be deleted)

DROP TABLE IF EXISTS date_night_idea;

-- ------------------------------------------
-- Table: date_night_idea
-- Stores all possible date night ideas
-- ------------------------------------------
CREATE TABLE date_night_idea (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    budget_category VARCHAR(20) NOT NULL CHECK (budget_category IN ('Free', 'Cheap', 'Moderate', 'Expensive')),
	--The below line creates a column named category_id INT, which means it will store integer values
	--corresponding to the id of a category in the category table. REFERENCES category(id) sets up a foreign key
	--relationship, which ensures referential integrity. It doesn't allow users to assign a non-existent category
	--to a date night idea. ON DELETE SET NULL informs PostgreSQL that if a category is deleted, set the category_id
	--to null instead of deleting it.
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_suggested BOOLEAN DEFAULT FALSE
);

-- Insert some sample date night ideas into the date_night_idea table
INSERT INTO date_night_idea (title, description, budget_category, location)
VALUES
('Picnic in the Park', 'Pack some snacks and enjoy a relaxing outdoor picnic.', 'Free', 'Local Park'),
('Stargazing at Night', 'Pack some snacks and enjoy romantic starlight.', 'Free', 'Local Park'),
('Go on a Hike', 'Take a trip to the closest park with hiking trails and walk together.', 'Free', 'Local Park'),
('Ride Bikes Together', 'Grab your bicycle and head to the nearest bike path.', 'Free', 'Local Bikepath'),
('Movie Night at Home', 'Watch a classic movie with homemade popcorn.', 'Free', 'Home'),
('Go to a High School Football Game', 'Cheer on your hometown football team as they score touchdowns.', 'Cheap', 'High School Football Stadium'),
('Eat Dinner at IKEA or Sam''s Club', 'Known for their inexpensive food, IKEA and Sam''s Club have some of the cheapest dinner prices you''ll ever find! Afterward,
you can stroll through the store to shop.', 'Cheap', 'IKEA/Sams Club'),
('DIY Art Night', 'Pick up art supplies and work together on a fun project!', 'Cheap', 'Home'),
('Farmer''s Market', 'Visit your local farmer''s market together.', 'Cheap', 'Local Town'),
('Visit your Local Coffee Shop', 'Visit your favorite coffee shop and share a warm cup of hot chocolate.', 'Cheap', 'Local Coffee Shop'),
('Dinner at a New Restaurant', 'Try a new cuisine at a local restaurant.', 'Moderate', 'Downtown'),
('Go Bowling Together', 'Try out your luck at bowling together. Are bumper lanes necessary or is your partner a secret bowling shark?', 'Moderate', 'Bowling Alley'),
('Axe Throwing', 'Go axe-throwing together and see who wins!', 'Moderate', 'Local Axe Throwing'),
('Ice Skating', 'Visit your local ice skating rink together.', 'Moderate','Local Ice Skating Rink'),
('Kayaking', 'Splash your paddles in the water together when kayaking!', 'Moderate', 'River'),
('Weekend getaway', 'Book a weekend trip to a nearby city or resort.', 'Expensive', 'Nearby City'),
('Spa Day', 'Splurge on yourself and your partner by getting a couples massage.','Expensive','Local Spa'),
('Escape Room', 'Are you ready for a challenge? Try an escape room with your partner!', 'Expensive', 'Local Escape Room'),
('See a Broadway Show', 'Do you or your partner LOVE musicals? If so, its time to visit your local performing arts center!', 'Expensive', 'Local Performing Arts Center'),
('Take a helicopter ride together!', 'Tour your city from thousands of feet in the air by taking a helicopter ride together', 'Expensive', 'Local Tour Company');


