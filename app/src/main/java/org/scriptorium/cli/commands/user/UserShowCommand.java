package org.scriptorium.cli.commands.user;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.services.UserService;
import picocli.CommandLine.Command;
import org.scriptorium.core.exceptions.DataAccessException;
import picocli.CommandLine.Parameters;

import java.util.Optional;

@Command(name = "show", description = "Shows details of a specific user by ID.")
public class UserShowCommand implements Runnable {

    private final UserService userService;

    @Parameters(index = "0", description = "The ID of the user to show.", paramLabel = "USER_ID")
    private Long userId;

    public UserShowCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        if (userId == null) {
            System.err.println("Error: User ID is required.");
            return;
        }

        try {
            Optional<User> userOptional = userService.findById(userId);

            if (userOptional.isPresent()) {
                System.out.println("User details:");
                System.out.println(userOptional.get());
            } else {
                System.out.println("User with ID " + userId + " not found.");
            }
        } catch (DataAccessException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
