package org.scriptorium.config;

import org.scriptorium.cli.commands.ImportBookCommand;
import org.scriptorium.core.factories.BookFactory;
import org.scriptorium.core.http.SimpleHttpClient;
import org.scriptorium.core.services.BookImportService;
import picocli.CommandLine;

import java.util.Scanner;

public class DependencyFactory implements CommandLine.IFactory {

    // 1. Create all long-lived service instances
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleHttpClient httpClient = new SimpleHttpClient();
    private final BookFactory bookFactory = new BookFactory();
    private final BookImportService bookImportService = new BookImportService(httpClient, bookFactory);

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        // 2. Check which command Picocli wants and inject the required services
        if (cls.isAssignableFrom(ImportBookCommand.class)) {
            return (K) new ImportBookCommand(bookImportService, scanner);
        }

        // 3. For commands with no dependencies, use the default constructor
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            // Fallback for Picocli's internal classes or other simple commands
            return CommandLine.defaultFactory().create(cls);
        }
    }
}
