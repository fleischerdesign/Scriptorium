# Scriptorium - A Library System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Picocli](https://img.shields.io/badge/Picocli-F05032?style=for-the-badge&logo=picocli&logoColor=white)

A robust, console-based (as of now) library management system developed in Java. Scriptorium allows for the management of users, authors, publishers, and books through an intuitive command-line interface. It also supports importing book data from external sources like the Open Library API.

## Features

*   **User Management:** Create, list, show, update, and delete user accounts. Secure password storage using BCrypt hashing.
*   **Book Management:** Add, list, show, update, and delete books.
*   **Author and Publisher Management:** Manage authors and publishers associated with books.
*   **Open Library Integration:** Import book data directly from the Open Library API to streamline data entry.
*   **Persistence:** Data is stored in a SQLite database.
*   **Interactive Shell:** A user-friendly interactive shell for easy command execution.

## Technologies

*   **Java:** The primary development language.
*   **Gradle:** Build automation tool for project management.
*   **Picocli:** A powerful framework for developing command-line applications.
*   **SQLite:** A lightweight, file-based database for data persistence.
*   **jBCrypt:** A Java implementation of the BCrypt password hashing algorithm for secure password storage.
*   **Jackson:** A JSON processing library for deserializing Open Library API responses.
*   **Mockito & JUnit 5:** For unit and integration testing.

## Setup

To set up and run the Scriptorium CLI locally, follow these steps:

### Prerequisites

*   **Java Development Kit (JDK) 21 or higher:** Ensure JDK is installed on your system and the `JAVA_HOME` environment variable is correctly set.
*   **Gradle:** Gradle is bundled with the project (`gradlew` wrapper), so no separate installation is required.

### Clone the Repository

```bash
git clone https://github.com/YourUsername/scriptorium.git
cd scriptorium
```

### Build the Project

Navigate to the project's root directory and build the project using Gradle:

```bash
./gradlew build
```
This command compiles the source code, runs tests, and creates the executable JAR files in the `app/build/libs/` directory.

## Usage

The Scriptorium CLI can be used in two ways: in interactive shell mode or by executing single commands.

### Interactive Shell Mode

Start the interactive shell by running the JAR without any arguments:

```bash
java -jar app/build/libs/app.jar
```

You will then see the `scriptorium>` prompt. Type `help` to see a list of available commands, or `exit` to quit the shell.

### Single Command Mode

Execute a single command directly by providing the command arguments after the JAR name:

```bash
java -jar app/build/libs/app.jar [command] [options]
```

**Examples:**

*   **Create a User:**
    ```bash
    java -jar app/build/libs/app.jar user create -f Philipp -l F -e philipp@example.com -w mySecurePassword123 -s "Musterstr. 1" -p 12345 -c Musterstadt -o Germany
    ```

*   **List All Users:**
    ```bash
    java -jar app/build/libs/app.jar user list
    ```

*   **Import a Book (from Open Library):**
    ```bash
    java -jar app/build/libs/app.jar book import -i OL12345678M # Example Open Library ID
    ```

*   **Show Help for a Specific Command:**
    ```bash
    java -jar app/build/libs/app.jar user --help
    ```

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
