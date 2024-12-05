document.addEventListener("DOMContentLoaded", function(){
const apiURL = "http://localhost:7090/register";
const userInfo = document.getElementById("userInfo")
fetch(apiURL)
    .then(response =>.json())
    .then(data =>{
    })catch(error => {console.log("error is coming")})
})



const backendURL = "http://localhost:7090";
let token = null;

// Handle Registration
document.getElementById("registerForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const fullName = document.getElementById("fullName").value;
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch(`${backendURL}/api/v1/users/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ fullName, username, password }),
    });

    if (response.ok) {
        alert("Registration successful! Please log in.");
    } else {
        alert("Registration failed.");
    }
});

// Handle Login (Optional for Protected Endpoints)
// Use similar logic as registration to fetch the token and store it in `token`.

// Handle Sending Messages
document.getElementById("messageForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const userQuestion = document.getElementById("userQuestion").value;

    const response = await fetch(`${backendURL}/api/v1/chat/sessions/ask-question`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ sessionId: 1, userQuestion }),
    });

    if (response.ok) {
        const message = await response.json();
        const messagesDiv = document.getElementById("messages");
        messagesDiv.innerHTML += `<p>${message.userQuestion}</p>`;
    } else {
        alert("Failed to send message.");
    }
});












let chatHistory = [];
let currentChat = [];

function startNewChat() {
    // Check if currentChat is not empty and not already in chatHistory
    if (currentChat.length > 0 && !chatHistory.some(chat => JSON.stringify(chat) === JSON.stringify(currentChat))) {
        chatHistory.push([...currentChat]); // Add a copy of currentChat to chatHistory
    }
    currentChat = []; // Clear currentChat
    updateChatHistory(); // Update the displayed chat history
    updateChatConversation(); // Update the displayed chat conversation
}

function sendMessage() {
    const userMessage = document.getElementById("userInput").value.trim();
    if (userMessage) {
        fetch('/api/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: userMessage })
        })
        .then(response => response.json())
        .then(data => {
            const aiaResponse = data.response;
            currentChat.push({ user: userMessage, aia: aiaResponse });
            document.getElementById("userInput").value = "";
            updateChatConversation();
        })
        .catch(error => console.error('Error:', error));
    }
}

function loadChat(index) {
    currentChat = chatHistory[index];
    updateChatConversation();
}

function updateChatHistory() {
    const chatHistoryDiv = document.getElementById("chatHistory");
    chatHistoryDiv.innerHTML = "";

    if (chatHistory.length === 0) {
        chatHistoryDiv.innerHTML = "<p>No chat history yet.</p>";
    } else {
        chatHistory.forEach((chat, index) => {
            const firstMessage = chat[0]?.user || "Unnamed Chat";
            const button = document.createElement("button");
            button.className = "btn btn-outline-secondary btn-sm d-block w-100 mb-2";
            button.textContent = firstMessage.length > 20
                ? `${firstMessage.substring(0, 20)}...`
                : firstMessage;
            button.onclick = () => loadChat(index);
            chatHistoryDiv.appendChild(button);
        });
    }
}

function updateChatConversation() {
    const chatConversationDiv = document.getElementById("chatConversation");
    chatConversationDiv.innerHTML = "";

    if (currentChat.length === 0) {
        chatConversationDiv.innerHTML = "<p>Start a conversation!</p>";
    } else {
        currentChat.forEach((message) => {
            const userDiv = document.createElement("div");
            userDiv.className = "chat-message user";
            userDiv.textContent = `YOU: ${message.user}`;
            chatConversationDiv.appendChild(userDiv);

            const aiaDiv = document.createElement("div");
            aiaDiv.className = "chat-message aia";
            aiaDiv.textContent = `AIA: ${message.aia}`;
            chatConversationDiv.appendChild(aiaDiv);
        });
    }
}

function handleKeyPress(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
}


function toggleSidebar() {
    const sidebarWrapper = document.getElementById("sidebarWrapper");
    sidebarWrapper.classList.toggle("collapsed");

    // Optionally, change button text
    const toggleButton = sidebarWrapper.querySelector("button");
    if (sidebarWrapper.classList.contains("collapsed")) {
        toggleButton.textContent = "Expand Sidebar";
    } else {
        toggleButton.textContent = "Collapse Sidebar";
    }
}

// Add an event listener for form submission
document.addEventListener('DOMContentLoaded', () => {
    const forms = document.querySelectorAll('form');

    forms.forEach((form) => {
        form.addEventListener('submit', (event) => {
            event.preventDefault(); // Prevent default form submission
            alert('Form submitted!');
        });
    });
});

