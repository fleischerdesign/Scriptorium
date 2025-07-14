package org.scriptorium.config;

import org.scriptorium.cli.commands.ImportBookCommand;
import org.scriptorium.core.factories.BookFactory;
import org.scriptorium.core.http.SimpleHttpClient;
import org.scriptorium.core.services.BookImportService;
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

    /**
     * Called by Picocli to create an instance of a command class.
     *
     * @param cls The class of the command to instantiate.
     * @param <K> The type of the command.
     * @return A fully configured instance of the command, with dependencies injected.
     * @throws Exception if instantiation fails.
     */
    @Override
    public <K> K create(Class<K> cls) throws Exception {
        // Check which command Picocli wants and inject the required services.
        if (cls.isAssignableFrom(ImportBookCommand.class)) {
            return (K) new ImportBookCommand(bookImportService, scanner);
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
