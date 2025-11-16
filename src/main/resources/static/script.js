// ----------------------------
// API URL
// ----------------------------
const apiUrl = "http://localhost:9090/api/date-night-ideas";

// ----------------------------
// DOM Elements - obtain elements using the Document Object Model
// ----------------------------
const results = document.getElementById("results-container");
const randomUI = document.getElementById("Random-idea-container");
const budgetSelect = document.getElementById("budget");
const generateBtn = document.getElementById("generate-idea-btn");
const modal = document.getElementById("idea-modal");
const modalClose = document.getElementById("modal-close");
const modalForm = document.getElementById("modal-form");
const modalTitle = document.getElementById("modal-title");
const modalDescription = document.getElementById("modal-description");
const modalBudget = document.getElementById("modal-budget");
const modalLocation = document.getElementById("modal-location");
const modalHeader = document.getElementById("modal-header");

let currentEditingId = null;

// ----------------------------
// Utility Functions - create helper methods for common tasks
// ----------------------------

//The clearResults function clears the list of results from the <ul> container
//so that old ideas don't pile up when you render new ones
function clearResults() {
    results.innerHTML = "";
}

//the show message function displays a message to the user in the results container
function showMessage(message) {
    results.innerHTML = `<li class="message">${message}</li>`;
}

// ----------------------------
// Fetch all ideas
// ----------------------------
//The fetchAllIdeas function retrieves all date night ideas from the backend API
//and displays them in the results container. It also hides the random idea UI section.
async function fetchAllIdeas() {
    try {
        randomUI.style.display = "none";
        // Fetch all ideas from the backend API. Await pauses execution until the server
        //responds, so res contains the response object
        const res = await fetch(`${apiUrl}/allIdeas`);

        //If the response is not ok (status code outside 200-299), throw an error
        if (!res.ok) throw new Error("Failed to fetch ideas");

        //Convert the raw HTTP response into JSON format
        const ideas = await res.json();

        //Calls the clear results function to empty the results container
        clearResults();

        //render each idea card by iterating over the ideas array
        ideas.forEach(renderIdeaCard);
    } catch (err) {
        console.error(err);
        showMessage("Failed to load ideas.");
    }
}

// ----------------------------
// Render idea card for CRUD
// ----------------------------
function renderIdeaCard(item) {
    //Create a new list item element to represent the idea card and add the "idea-card" class to it
    const li = document.createElement("li");
    li.classList.add("idea-card");

    //Set the inner HTML of the list item to display the idea's details and action buttons
    li.innerHTML = `
        <h3>${item.title}</h3>
        <p>${item.description || ""}</p>
        <p><strong>Budget:</strong> ${item.budgetCategory || "Unknown"}</p>
        <p><strong>Location:</strong> ${item.location || "Anywhere"}</p>
        <div class="btn-group">
            <button class="update-btn">Update</button>
            <button class="delete-btn">Delete</button>
        </div>
    `;

    // Add an event listener to the update button for clicks. If clicked, currentEditingId
    //is a global variable that stores the ID of the idea being edited. When the update button is clicked,
    //the modal form is populated with the idea's existing data and displayed to the user.
    li.querySelector(".update-btn").addEventListener("click", () => {
        currentEditingId = item.id;

        //modalHeader is the title of the modal form, which is set to "Update Idea"
        modalHeader.textContent = "Update Idea";

        //The following lines populate the modal form fields with the existing idea data
        modalTitle.value = item.title;
        modalDescription.value = item.description || "";
        modalBudget.value = item.budgetCategory || "Free";
        modalLocation.value = item.location || "";

        //The below line changes the display style of the modal to "block", making it visible to the user
        modal.style.display = "block";
    });

    // Add an event listener to the delete button for clicks. When clicked, a confirmation dialog
    //is shown to the user. If the user confirms, a DELETE request is sent to the backend API
    //to delete the idea. Upon successful deletion, a success message is shown and the list of ideas is refreshed.
    li.querySelector(".delete-btn").addEventListener("click", async () => {
        if (confirm("Are you sure you want to delete this idea?")) {
            try {
                const res = await fetch(`${apiUrl}/deleteIdea/${item.id}`, { method: "DELETE" });
                if (!res.ok) throw new Error("Failed to delete idea");
                showMessage("Idea deleted successfully!");
                // Refresh the list of ideas after deletion
                fetchAllIdeas();
            } catch (err) {
                console.error(err);
                showMessage("Failed to delete idea.");
            }
        }
    });

    //Append the constructed idea card list item to the results container in the DOM
    results.appendChild(li);
}

// ----------------------------
// Fetch random idea
// ----------------------------

//Create a function to fetch a random date night idea based on the selected budget category. The async
//keyword allows the use of await within the function to handle asynchronous operations.
async function fetchRandomIdea() {
    //the below line makes the random idea UI section visible by setting its display style to "block"
    randomUI.style.display = "block";

    //Get the selected budget category from the budget select dropdown
    const budget = budgetSelect.value;

    //Use a try-catch block to handle potential errors during the fetch operation from the backend API
    try {
        const res = await fetch(`${apiUrl}/random/${budget}`);
        if (res.status === 204) { //204 is a No Content response
            showMessage("No ideas available for this budget. Please reset the list.");
            return;
        }
        //if the response is not ok (status code outside 200-299), throw an error
        if (!res.ok) throw new Error("Failed to fetch random idea"); 

        //Convert the raw HTTP response into JSON format and store it in the idea variable
        const idea = await res.json();

        //Clear any existing results and render the fetched random idea card
        clearResults();
        renderIdeaCard(idea);
    } catch (err) {
        console.error(err);
        showMessage("Failed to fetch random idea.");
    }
}

// ----------------------------
// Reset ideas
// ----------------------------
//The resetIdeas function sends a POST request to the backend API to reset the list of date night ideas
//to its original state. Upon successful reset, it shows a success message and fetches all ideas to refresh the list.
//While PUT is typically used for updates, POST is used here to trigger the reset action. This is because it is a global
//action that doesn't fit the typical CRUD operations associated with a specific resource.
async function resetIdeas() {
    try {
        //Send a POST request to the /reset endpoint of the backend API
        const res = await fetch(`${apiUrl}/reset`, { method: "POST" });

        //If the response is not ok (status code outside 200-299), throw an error
        if (!res.ok) throw new Error("Failed to reset ideas");

        //Convert the raw HTTP response into text format to get the success message
        const message = await res.text();
        showMessage(message); // Show reset success message

        //Fetch all ideas to refresh the list after reset
        fetchAllIdeas();
    } catch (err) {
        console.error(err);
        showMessage("Failed to reset ideas.");
    }
}

// ----------------------------
// Modal form submit (Add / Update)
// ----------------------------
//The modal form submit event listener handles both adding new ideas and updating existing ones. When the form is submitted, it prevents the default form submission behavior,
//collects the form data, and sends either a POST request to add a new idea or a PUT request to update an existing idea
//based on whether currentEditingId is set. After a successful operation, it closes the modal, resets the form,
//and refreshes the list of ideas.
modalForm.addEventListener("submit", async (e) => {
    //Prevent the default form submission behavior
    e.preventDefault();

    //Collect the form data into an ideaData object
    const ideaData = {
        title: modalTitle.value,
        description: modalDescription.value,
        budgetCategory: modalBudget.value,
        location: modalLocation.value
    };

    //Use a try-catch block to handle potential errors during the add/update operation
    try {
        if (currentEditingId) {
            // Update single idea
            const res = await fetch(`${apiUrl}/updateIdea/${currentEditingId}`, {
                method: "PUT",

                //Content-Ty[e tells the server what kind of data you're sending and
                //body is the actual data sent. Stringify converts the data into a JSON format
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(ideaData)
            });
            if (!res.ok) throw new Error("Failed to update idea");
            showMessage("Idea updated successfully!");

            //If the idea doesn't have an ID, then it is a new idea.
        } else {
            // Add new idea
            const res = await fetch(`${apiUrl}/addIdea`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(ideaData)
            });
            if (!res.ok) throw new Error("Failed to add idea");
            showMessage("Idea added successfully!");
        }

        //Hide the modal from the user interface
        modal.style.display = "none";

        //Reset all input fields in the form
        modalForm.reset();

        //Reset the tracking variable to know whether the form is adding a new idea or updating an existing one
        currentEditingId = null;

        //Refresh the list of ideas being shown
        fetchAllIdeas();
    } catch (err) {
        console.error(err);
        showMessage("Failed to save idea.");
    }
});

// ----------------------------
// Modal close
// ----------------------------
//Add an event listener to the close modal button
modalClose.addEventListener("click", () => {

    //Hide the modal from the user, reset the form, and reset the editing id to null
    modal.style.display = "none";
    modalForm.reset();
    currentEditingId = null;
});

// ----------------------------
// Event listeners
// ----------------------------

//If generate idea is clicked, run the fetchRandomIdea function
document.getElementById("generate-idea-btn").addEventListener("click", fetchRandomIdea);

//If the create button is clicked, change the editing id to null and the modal header to add idea
//Reset the form and display the modal
document.getElementById("create-button").addEventListener("click", () => {
    currentEditingId = null;
    modalHeader.textContent = "Add Idea";
    modalForm.reset();
    modal.style.display = "block";
});

//Create event listeners for update, delete, reset, and random and respond with the appropriate 
//functions accordingly
document.getElementById("update-button").addEventListener("click", fetchAllIdeas);
document.getElementById("delete-button").addEventListener("click", fetchAllIdeas);
document.getElementById("reset-button").addEventListener("click", resetIdeas);
document.getElementById("random-btn").addEventListener("click", () => {
    randomUI.style.display = "block";
    clearResults();
});
