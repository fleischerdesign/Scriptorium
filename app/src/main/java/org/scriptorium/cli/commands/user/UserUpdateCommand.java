package org.scriptorium.cli.commands.user;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.services.UserService;
import picocli.CommandLine.Command;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Optional;

@Command(name = "update", description = "Updates an existing user.")
public class UserUpdateCommand implements Runnable {

    private final UserService userService;

    @Parameters(index = "0", description = "The ID of the user to update.", paramLabel = "USER_ID")
    private Long userId;

    @Option(names = {"-f", "--firstName"}, description = "User's first name")
    private String firstName;

    @Option(names = {"-l", "--lastName"}, description = "User's last name")
    private String lastName;

    @Option(names = {"-e", "--email"}, description = "User's email address (must be unique)")
    private String email;

    @Option(names = {"-s", "--street"}, description = "User's street")
    private String street;

    @Option(names = {"-p", "--postalCode"}, description = "User's postal code")
    private String postalCode;

    @Option(names = {"-c", "--city"}, description = "User's city")
    private String city;

    @Option(names = {"-o", "--country"}, description = "User's country")
    private String country;

    public UserUpdateCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        if (userId == null) {
            System.err.println("Error: User ID is required for update.");
            return;
        }

        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            System.out.println("User with ID " + userId + " not found.");
            return;
        }

        User userToUpdate = userOptional.get();

        // Update fields only if they are provided
        if (firstName != null) {
            userToUpdate.setFirstName(firstName);
        }
        if (lastName != null) {
            userToUpdate.setLastName(lastName);
        }
        if (email != null) {
            userToUpdate.setEmail(email);
        }
        if (street != null) {
            userToUpdate.setStreet(street);
        }
        if (postalCode != null) {
            userToUpdate.setPostalCode(postalCode);
        }
        if (city != null) {
            userToUpdate.setCity(city);
        }
        if (country != null) {
            userToUpdate.setCountry(country);
        }

        try {
            User updatedUser = userService.save(userToUpdate);
            System.out.println("User updated successfully: " + updatedUser);
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (DataAccessException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
