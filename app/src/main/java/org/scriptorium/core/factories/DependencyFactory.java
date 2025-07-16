package org.scriptorium.core.factories;

import org.scriptorium.cli.commands.book.BookImportCommand;
import org.scriptorium.core.http.SimpleHttpClient;
import org.scriptorium.core.services.BookImportService;
import org.scriptorium.cli.BookCommand;
import org.scriptorium.cli.UserCommand;
import org.scriptorium.cli.commands.user.UserListCommand;
import org.scriptorium.cli.commands.user.UserCreateCommand;
import org.scriptorium.cli.commands.user.UserShowCommand;
import org.scriptorium.cli.commands.user.UserDeleteCommand;
import org.scriptorium.cli.commands.user.UserUpdateCommand;
import org.scriptorium.core.repositories.JdbcUserRepository;
import org.scriptorium.core.repositories.UserRepository;
import org.scriptorium.core.services.UserService;
import picocli.CommandLine;

import java.util.Scanner;

/**
 * A custom factory for Picocli that handles dependency injection for command classes.
 *
 * This class acts as the application's "Composition Root," creating and wiring all
 * long-lived services (like {@link BookImportService}, {@link SimpleHttpClient}, etc.)
 * and injecting them into the command instances that need them.
 */
public class DependencyFactory implements CommandLine.IFactory {

    // Single, long-lived instances of all application services.
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleHttpClient httpClient = new SimpleHttpClient();
    private final BookFactory bookFactory = new BookFactory();
    private final BookImportService bookImportService = new BookImportService(httpClient, bookFactory);
    private final UserRepository userRepository = new JdbcUserRepository("jdbc:sqlite:scriptorium.db");
    private final UserService userService = new UserService(userRepository);

    /**
     * Called by Picocli to create an instance of a command class.
     *
     * @param cls The class of the command to instantiate.
     * @param <K> The type of the command.
     * @return A fully configured instance of the command, with dependencies injected.
     * @throws Exception if instantiation fails.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <K> K create(Class<K> cls) throws Exception {
        // Check which command Picocli wants and inject the required services.
        if (cls.isAssignableFrom(BookImportCommand.class)) {
            return (K) new BookImportCommand(bookImportService, scanner);
        }
        if (cls.isAssignableFrom(UserListCommand.class)) {
            return (K) new UserListCommand(userService);
        }
        if (cls.isAssignableFrom(UserCreateCommand.class)) {
            return (K) new UserCreateCommand(userService);
        }
        if (cls.isAssignableFrom(UserShowCommand.class)) {
            return (K) new UserShowCommand(userService);
        }
        if (cls.isAssignableFrom(UserDeleteCommand.class)) {
            return (K) new UserDeleteCommand(userService);
        }
        if (cls.isAssignableFrom(UserUpdateCommand.class)) {
            return (K) new UserUpdateCommand(userService);
        }
        if (cls.isAssignableFrom(UserCommand.class)) {
            return (K) new UserCommand();
        }
        if (cls.isAssignableFrom(BookCommand.class)) {
            return (K) new BookCommand();
        }

        // For commands with no dependencies, or for Picocli's internal classes,
        // fall back to the default factory behavior.
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return CommandLine.defaultFactory().create(cls);
        }
    }
}
