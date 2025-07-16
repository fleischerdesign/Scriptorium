package org.scriptorium.core.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scriptorium.core.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.scriptorium.core.exceptions.DuplicateEmailException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JdbcUserRepositoryTest {

    private static final String TEST_DB_URL = "jdbc:sqlite::memory:"; // In-memory database for testing
    private JdbcUserRepository userRepository;

    @BeforeEach
    void setUp() throws SQLException {
        userRepository = new JdbcUserRepository(TEST_DB_URL);
        // Ensure the table is clean before each test
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users;");
        }
        // Re-initialize the repository to create the table in the in-memory DB
        userRepository = new JdbcUserRepository(TEST_DB_URL);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up after each test
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users;");
        }
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("Jane", "Doe", "jane.doe@example.com");
        user.setStreet("Main St");
        user.setPostalCode("12345");
        user.setCity("Anytown");
        user.setCountry("USA");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Jane", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("jane.doe@example.com", savedUser.getEmail());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testUpdateUser() {
        User user = new User("John", "Doe", "john.doe@example.com");
        User savedUser = userRepository.save(user);

        savedUser.setFirstName("Jonathan");
        savedUser.setEmail("jonathan.doe@example.com");

        User updatedUser = userRepository.save(savedUser);

        assertEquals("Jonathan", updatedUser.getFirstName());
        assertEquals("jonathan.doe@example.com", updatedUser.getEmail());

        Optional<User> foundUser = userRepository.findById(updatedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Jonathan", foundUser.get().getFirstName());
        assertEquals("jonathan.doe@example.com", foundUser.get().getEmail());
    }

    @Test
    void testUpdateUserPartial() {
        User user = new User("Original", "Name", "original@example.com");
        user.setStreet("Old Street");
        user.setPostalCode("11111");
        user.setCity("Old City");
        user.setCountry("Old Country");
        User savedUser = userRepository.save(user);

        savedUser.setFirstName("Updated");
        savedUser.setEmail("updated@example.com");
        // Other fields remain unchanged

        User updatedUser = userRepository.save(savedUser);

        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("Name", updatedUser.getLastName()); // Should be unchanged
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("Old Street", updatedUser.getStreet()); // Should be unchanged
        assertEquals("11111", updatedUser.getPostalCode()); // Should be unchanged
        assertEquals("Old City", updatedUser.getCity()); // Should be unchanged
        assertEquals("Old Country", updatedUser.getCountry()); // Should be unchanged

        Optional<User> foundUser = userRepository.findById(updatedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Updated", foundUser.get().getFirstName());
        assertEquals("Name", foundUser.get().getLastName());
        assertEquals("updated@example.com", foundUser.get().getEmail());
        assertEquals("Old Street", foundUser.get().getStreet());
        assertEquals("11111", foundUser.get().getPostalCode());
        assertEquals("Old City", foundUser.get().getCity());
        assertEquals("Old Country", foundUser.get().getCountry());
    }

    @Test
    void testFindAll() {
        User user1 = new User("Alice", "Smith", "alice@example.com");
        User user2 = new User("Bob", "Johnson", "bob@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("alice@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("bob@example.com")));
    }

    @Test
    void testDeleteById() {
        User user = new User("Charlie", "Brown", "charlie@example.com");
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveDuplicateEmailThrowsException() {
        User user1 = new User("Test", "User", "duplicate@example.com");
        userRepository.save(user1);

        User user2 = new User("Another", "User", "duplicate@example.com");

        assertThrows(DuplicateEmailException.class, () -> {
            userRepository.save(user2);
        });
    }

    @Test
    void testUpdateToDuplicateEmailThrowsException() {
        User user1 = new User("Test", "User", "email1@example.com");
        userRepository.save(user1);

        User user2 = new User("Another", "User", "email2@example.com");
        User savedUser2 = userRepository.save(user2);

        savedUser2.setEmail("email1@example.com"); // Try to update to an existing email

        assertThrows(DuplicateEmailException.class, () -> {
            userRepository.save(savedUser2);
        });
    
        User user = new User("Full", "Name", "full.name@example.com");
        user.setStreet("123 Test St");
        user.setPostalCode("98765");
        user.setCity("Testville");
        user.setCountry("Testland");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Full", savedUser.getFirstName());
        assertEquals("Name", savedUser.getLastName());
        assertEquals("full.name@example.com", savedUser.getEmail());
        assertEquals("123 Test St", savedUser.getStreet());
        assertEquals("98765", savedUser.getPostalCode());
        assertEquals("Testville", savedUser.getCity());
        assertEquals("Testland", savedUser.getCountry());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("123 Test St", foundUser.get().getStreet());
        assertEquals("98765", foundUser.get().getPostalCode());
        assertEquals("Testville", foundUser.get().getCity());
        assertEquals("Testland", foundUser.get().getCountry());
    }
}
