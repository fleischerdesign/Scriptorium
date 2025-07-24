package org.scriptorium.core.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.scriptorium.core.domain.User;
import org.scriptorium.core.repositories.UserRepository;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User("Test", "User", "test@example.com", "password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsDuplicateEmailException() {
        User user = new User("Test", "User", "test@example.com", "password");
        when(userRepository.save(any(User.class))).thenThrow(new DuplicateEmailException("Email already exists"));

        DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> {
            userService.createUser(user);
        });
        assertTrue(thrown.getMessage().contains("User with email test@example.com already exists."));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsDataAccessException() {
        User user = new User("Test", "User", "test@example.com", "password");
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("DB error"));

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            userService.createUser(user);
        });
        assertTrue(thrown.getMessage().contains("DB error"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByIdFound() {
        User user = new User("Test", "User", "test@example.com", "password");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findById(99L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User("Alice", "Smith", "alice@example.com", "pass1");
        User user2 = new User("Bob", "Johnson", "bob@example.com", "pass2");
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.findAll();

        assertFalse(foundUsers.isEmpty());
        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        User user = new User("Updated", "User", "updated@example.com", "newPass");
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals("updated@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserWithoutIdThrowsException() {
        User user = new User("New", "User", "new@example.com", "password");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user);
        });

        assertEquals("User must have an ID to be updated.", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUserWithPartialFields() {
        User existingUser = new User("Original", "Name", "original@example.com", "originalPass");
        existingUser.setId(1L);
        existingUser.setStreet("Old Street");
        existingUser.setPostalCode("11111");
        existingUser.setCity("Old City");
        existingUser.setCountry("Old Country");

        User updatedUser = new User("Updated", "Name", "updated@example.com", "updatedPass");
        updatedUser.setId(1L);
        updatedUser.setStreet("New Street");
        updatedUser.setPostalCode("22222");
        updatedUser.setCity("New City");
        updatedUser.setCountry("New Country");

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        assertEquals("New Street", result.getStreet());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testAuthenticateUserSuccess() {
        String email = "test@example.com";
        String password = "password";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User("Test", "User", email, hashedPassword);
        user.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> authenticatedUser = userService.authenticateUser(email, password);

        assertTrue(authenticatedUser.isPresent());
        assertEquals(user.getEmail(), authenticatedUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testAuthenticateUserFailureWrongPassword() {
        String email = "test@example.com";
        String password = "password";
        String wrongPassword = "wrongpassword";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User("Test", "User", email, hashedPassword);
        user.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> authenticatedUser = userService.authenticateUser(email, wrongPassword);

        assertFalse(authenticatedUser.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testAuthenticateUserFailureUserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> authenticatedUser = userService.authenticateUser(email, password);

        assertFalse(authenticatedUser.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
