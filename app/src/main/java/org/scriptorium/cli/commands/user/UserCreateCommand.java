package org.scriptorium.cli.commands.user;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.services.UserService;
import picocli.CommandLine.Command;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.exceptions.DuplicateEmailException;
import picocli.CommandLine.Option;

@Command(name = "create", description = "Creates a new user.")
public class UserCreateCommand implements Runnable {

    private final UserService userService;

    @Option(names = {"-f", "--firstName"}, description = "User's first name", required = true, interactive = true, echo = true)
    private String firstName;

    @Option(names = {"-l", "--lastName"}, description = "User's last name", required = true, interactive = true, echo = true)
    private String lastName;

    @Option(names = {"-e", "--email"}, description = "User's email address (must be unique)", required = true, interactive = true, echo = true)
    private String email;

    @Option(names = {"-w", "--password"}, description = "User's password", required = true, interactive = true, arity = "0..1")
    private String password;

    @Option(names = {"-s", "--street"}, description = "User's street")
    private String street;

    @Option(names = {"-p", "--postalCode"}, description = "User's postal code")
    private String postalCode;

    @Option(names = {"-c", "--city"}, description = "User's city")
    private String city;

    @Option(names = {"-o", "--country"}, description = "User's country")
    private String country;

    public UserCreateCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPasswordHash(password); // Set the plain-text password, UserService will hash it
        newUser.setStreet(street);
        newUser.setPostalCode(postalCode);
        newUser.setCity(city);
        newUser.setCountry(country);

        try {
            User createdUser = userService.save(newUser);
            System.out.println("User created successfully: " + createdUser);
        } catch (DuplicateEmailException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (DataAccessException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
