# Scriptorium - A Library System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Javalin](https://img.shields.io/badge/Javalin-00B0D8?style=for-the-badge&logo=javalin&logoColor=white)
![Picocli](https://img.shields.io/badge/Picocli-F05032?style=for-the-badge&logo=picocli&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)

Scriptorium is a robust library management system developed in Java. It provides both a feature-rich command-line interface (CLI) and a REST API for managing books, users, loans, and more.

## Features

### Core Features
- **Inventory Management:** Manage books, authors, publishers, genres, and physical copies.
- **User Management:** Create, list, show, update, and delete user accounts with secure password storage using BCrypt.
- **Loan System:** Create and manage book loans and returns.
- **Reservation System:** Create, cancel, and fulfill book reservations.
- **Data Import:** Import book data from the Open Library API via ISBN or Open Library ID.
- **Persistence:** All data is stored in a SQLite database.

### Command-Line Interface (CLI)
- **Interactive Shell:** A user-friendly interactive shell (`scriptorium>`) for easy command execution.
- **Comprehensive Commands:** Detailed commands for all core features (e.g., `book create`, `user list`, `loan return`).
- **Server Management:** Start and stop the API server directly from the CLI.

### REST API
- **RESTful Interface:** A standards-compliant REST API for programmatic management of all library resources.
- **JSON Format:** All API responses are in JSON format.
- **CRUD Operations:** Standard Create, Read, Update, and Delete endpoints for most resources.
- **Specific Actions:** Dedicated endpoints for actions like loaning or reserving books.

## Technologies

- **Java 21:** The primary development language.
- **Gradle:** Build automation tool.
- **Javalin:** A lightweight web framework for the REST API.
- **Picocli:** A powerful framework for building command-line applications.
- **SQLite:** A lightweight, file-based database for data persistence.
- **Jackson:** A JSON library for serializing and deserializing API data.
- **jBCrypt:** A Java implementation of the BCrypt password hashing algorithm.
- **JUnit 5 & Mockito:** For unit and integration testing.

## Setup

To set up and run Scriptorium locally, follow these steps:

### Prerequisites

- **Java Development Kit (JDK) 21 or higher:** Ensure JDK is installed on your system and the `JAVA_HOME` environment variable is correctly set.
- **Gradle:** Gradle is bundled with the project (`gradlew` wrapper), so no separate installation is required.

### Clone the Repository

```bash
git clone <repository_url>
cd scriptorium
```

### Build the Project

Navigate to the project's root directory and build the project using Gradle:

```bash
./gradlew build
```
This command compiles the source code, runs tests, and creates the executable JAR file in the `app/build/libs/` directory.

## Usage

### Command-Line Interface (CLI)

The Scriptorium CLI can be used in two ways: in interactive shell mode or by executing single commands. The executable JAR is located at `app/build/libs/app.jar`.

**Interactive Shell Mode**

Start the interactive shell by running the JAR file without any arguments:
```bash
java -jar app/build/libs/app.jar
```
You will then see the `scriptorium>` prompt. Type `help` to see a list of available commands, or `exit` to quit the shell.

**Single Command Mode**

Execute a single command directly by providing the command arguments after the JAR name:
```bash
java -jar app/build/libs/app.jar [command] [subcommand] [options]
```
**Examples:**
```bash
# Create a user
java -jar app/build/libs/app.jar user create -f Max -l Mustermann -e max@example.com -w mySecurePassword123

# List all books
java -jar app/build/libs/app.jar book list

# Import a book from Open Library
java -jar app/build/libs/app.jar book import --olid OL7353617M
```

### REST API Server

The API server can be started and stopped via the CLI.

**Start the Server**

Start the API server with the following command. It runs on port `7070` by default.
```bash
java -jar app/build/libs/app.jar server start
```
You can specify a different port:
```bash
java -jar app/build/libs/app.jar server start --port 8080
```
The server starts as a background process. Logs are written to `server.log` and `server.err`, and a process ID file (`scriptorium.pid`) is created.

**Stop the Server**

```bash
java -jar app/build/libs/app.jar server stop
```

### API Endpoints

The API provides RESTful endpoints to manage resources. Here are a few examples:

- **Books:**
  - `GET /api/books` - Retrieves all books.
  - `GET /api/books/{id}` - Retrieves a specific book.
  - `POST /api/books` - Creates a new book.
- **Users:**
  - `GET /api/users` - Retrieves all users.
  - `POST /api/users` - Creates a new user.
- **Loans:**
  - `POST /api/loans` - Creates a new loan (borrows a book).
  - `POST /api/loans/return` - Returns a book.

The path prefixes for other resources (`authors`, `publishers`, `genres`, `copies`, `reservations`) follow a similar pattern.