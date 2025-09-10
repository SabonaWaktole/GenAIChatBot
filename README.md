## 🤖 GenAI Chatbot (Gemini.js + Spring Boot + PostgreSQL)

An **AI-powered chatbot prototype** that integrates **Google Gemini API** with a **Spring Boot backend** and **PostgreSQL database**.
This project demonstrates how to build scalable conversational AI applications with real-time inference and persistent memory.

---

## ✨ Features

* 🧠 **Generative AI Responses** — Powered by Gemini API for contextual, human-like conversations.
* 🔄 **Persistent Memory** — Stores chat history in PostgreSQL for multi-session support.
* ⚡ **Scalable Backend** — Spring Boot APIs designed for concurrency and future growth.
* 💻 **Full-Stack Integration** — Gemini.js frontend connected with RESTful Spring Boot services.

---

## 🛠️ Tech Stack

* **Frontend:** [Gemini.js](https://ai.google.dev/)
* **Backend:** Spring Boot (Java)
* **Database:** PostgreSQL
* **AI Model:** Google Gemini API
* **Build Tools:** Maven / Gradle

---

## 🚀 Getting Started

### 1. Clone the repo

```bash
git clone [https://github.com/yourusername/genai-chatbot](https://github.com/SabonaWaktole/GenAIChatBot.git
```

### 2. Backend Setup (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

### 3. Frontend Setup (Gemini.js)

```bash
cd frontend
npm install
npm run dev
```

### 4. Database Setup (PostgreSQL)

* Create a database (e.g., `chatbot_db`)
* Update `application.properties` in Spring Boot with your DB credentials
* Run migrations from `db/`

---

## 🔑 Environment Variables

Create a `.env` file in the backend:

```env
GEMINI_API_KEY=your_api_key_here
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/chatbot_db
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
```

---

## 📸 Demo Screenshot (Optional)

*Add a screenshot or GIF of your chatbot in action*

---

## 📌 Roadmap

* [ ] Add user authentication
* [ ] Improve UI styling for chat interface
* [ ] Deploy to cloud (AWS/GCP)
* [ ] Add support for multiple languages

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you’d like to change.

---
