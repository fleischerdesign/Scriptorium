package org.scriptorium.cli.commands.user;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.services.UserService;
import picocli.CommandLine.Command;

import java.util.List;

@Command(name = "list", description = "Lists all users.")
public class UserListCommand implements Runnable {

    private final UserService userService;

    public UserListCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }
}
