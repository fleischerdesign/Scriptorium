package org.scriptorium.cli.commands.user;

import org.scriptorium.core.services.UserService;
import picocli.CommandLine.Command;
import org.scriptorium.core.exceptions.DataAccessException;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Deletes a user by ID.")
public class UserDeleteCommand implements Runnable {

    private final UserService userService;

    @Parameters(index = "0", description = "The ID of the user to delete.", paramLabel = "USER_ID")
    private Long userId;

    public UserDeleteCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        if (userId == null) {
            System.err.println("Error: User ID is required.");
            return;
        }

        try {
            if (userService.findById(userId).isEmpty()) {
                System.out.println("User with ID " + userId + " not found. No deletion performed.");
                return;
            }
            userService.deleteById(userId);
            System.out.println("User with ID " + userId + " deleted successfully.");
        } catch (DataAccessException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
