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
        User user = new User("Test", "User", "test@example.com");
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserThrowsDuplicateEmailException() {
        User user = new User("Test", "User", "test@example.com");
        when(userRepository.save(user)).thenThrow(new DuplicateEmailException("Email already exists"));

        DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> {
            userService.createUser(user);
        });
        assertTrue(thrown.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserThrowsDataAccessException() {
        User user = new User("Test", "User", "test@example.com");
        when(userRepository.save(user)).thenThrow(new DataAccessException("DB error"));

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            userService.createUser(user);
        });
        assertTrue(thrown.getMessage().contains("DB error"));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByIdFound() {
        User user = new User("Test", "User", "test@example.com");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findUserById(99L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User("Alice", "Smith", "alice@example.com");
        User user2 = new User("Bob", "Johnson", "bob@example.com");
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.findAllUsers();

        assertFalse(foundUsers.isEmpty());
        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        User user = new User("Updated", "User", "updated@example.com");
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals("updated@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserWithoutIdThrowsException() {
        User user = new User("New", "User", "new@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(user);
        });

        assertEquals("User must have an ID to be updated.", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUserWithPartialFields() {
        User existingUser = new User("Original", "Name", "original@example.com");
        existingUser.setId(1L);
        existingUser.setStreet("Old Street");
        existingUser.setPostalCode("11111");
        existingUser.setCity("Old City");
        existingUser.setCountry("Old Country");

        User updatedUser = new User("Updated", "Name", "updated@example.com");
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
}
