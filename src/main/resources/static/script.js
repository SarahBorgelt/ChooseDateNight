// ----------------------------
// API URL
// ----------------------------
const apiUrl = "http://localhost:9090/api/date-night-ideas";

// ----------------------------
// DOM Elements
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
// Utility Functions
// ----------------------------
function clearResults() {
    results.innerHTML = "";
}

function showMessage(message) {
    results.innerHTML = `<li class="message">${message}</li>`;
}

// ----------------------------
// Fetch all ideas
// ----------------------------
async function fetchAllIdeas() {
    try {
        randomUI.style.display = "none";
        const res = await fetch(`${apiUrl}/allIdeas`);
        if (!res.ok) throw new Error("Failed to fetch ideas");
        const ideas = await res.json();
        clearResults();
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
    const li = document.createElement("li");
    li.classList.add("idea-card");

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

    // Update button
    li.querySelector(".update-btn").addEventListener("click", () => {
        currentEditingId = item.id;
        modalHeader.textContent = "Update Idea";
        modalTitle.value = item.title;
        modalDescription.value = item.description || "";
        modalBudget.value = item.budgetCategory || "Free";
        modalLocation.value = item.location || "";
        modal.style.display = "block";
    });

    // Delete button
    li.querySelector(".delete-btn").addEventListener("click", async () => {
        if (confirm("Are you sure you want to delete this idea?")) {
            try {
                const res = await fetch(`${apiUrl}/deleteIdea/${item.id}`, { method: "DELETE" });
                if (!res.ok) throw new Error("Failed to delete idea");
                showMessage("Idea deleted successfully!");
                fetchAllIdeas();
            } catch (err) {
                console.error(err);
                showMessage("Failed to delete idea.");
            }
        }
    });

    results.appendChild(li);
}

// ----------------------------
// Fetch random idea
// ----------------------------
async function fetchRandomIdea() {
    randomUI.style.display = "block";
    const budget = budgetSelect.value;

    try {
        const res = await fetch(`${apiUrl}/random/${budget}`);
        if (res.status === 204) {
            showMessage("No ideas available for this budget. Please reset the list.");
            return;
        }
        if (!res.ok) throw new Error("Failed to fetch random idea");
        const idea = await res.json();
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
async function resetIdeas() {
    try {
        const res = await fetch(`${apiUrl}/reset`, { method: "POST" });
        if (!res.ok) throw new Error("Failed to reset ideas");
        const message = await res.text();
        showMessage(message); // Show reset success message
        fetchAllIdeas();
    } catch (err) {
        console.error(err);
        showMessage("Failed to reset ideas.");
    }
}

// ----------------------------
// Modal form submit (Add / Update)
// ----------------------------
modalForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const ideaData = {
        title: modalTitle.value,
        description: modalDescription.value,
        budgetCategory: modalBudget.value,
        location: modalLocation.value
    };

    try {
        if (currentEditingId) {
            // Update single idea
            const res = await fetch(`${apiUrl}/updateIdea/${currentEditingId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(ideaData)
            });
            if (!res.ok) throw new Error("Failed to update idea");
            showMessage("Idea updated successfully!");
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

        modal.style.display = "none";
        modalForm.reset();
        currentEditingId = null;
        fetchAllIdeas();
    } catch (err) {
        console.error(err);
        showMessage("Failed to save idea.");
    }
});

// ----------------------------
// Modal close
// ----------------------------
modalClose.addEventListener("click", () => {
    modal.style.display = "none";
    modalForm.reset();
    currentEditingId = null;
});

// ----------------------------
// Event listeners
// ----------------------------
document.getElementById("generate-idea-btn").addEventListener("click", fetchRandomIdea);
document.getElementById("create-button").addEventListener("click", () => {
    currentEditingId = null;
    modalHeader.textContent = "Add Idea";
    modalForm.reset();
    modal.style.display = "block";
});
document.getElementById("update-button").addEventListener("click", fetchAllIdeas);
document.getElementById("delete-button").addEventListener("click", fetchAllIdeas);
document.getElementById("reset-button").addEventListener("click", resetIdeas);
document.getElementById("random-btn").addEventListener("click", () => {
    randomUI.style.display = "block";
    clearResults();
});
