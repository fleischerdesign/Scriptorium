package org.scriptorium.core.factories;

import org.scriptorium.cli.commands.author.AuthorCommand;
import org.scriptorium.cli.commands.author.AuthorCreateCommand;
import org.scriptorium.cli.commands.author.AuthorDeleteCommand;
import org.scriptorium.cli.commands.author.AuthorListCommand;
import org.scriptorium.cli.commands.author.AuthorShowCommand;
import org.scriptorium.cli.commands.author.AuthorUpdateCommand;
import org.scriptorium.cli.commands.book.BookCommand;
import org.scriptorium.cli.commands.book.BookCreateCommand;
import org.scriptorium.cli.commands.book.BookDeleteCommand;
import org.scriptorium.cli.commands.book.BookImportCommand;
import org.scriptorium.cli.commands.book.BookListCommand;
import org.scriptorium.cli.commands.book.BookShowCommand;
import org.scriptorium.cli.commands.book.BookUpdateCommand;
import org.scriptorium.cli.commands.genre.GenreCommand;
import org.scriptorium.cli.commands.genre.GenreCreateCommand;
import org.scriptorium.cli.commands.genre.GenreDeleteCommand;
import org.scriptorium.cli.commands.genre.GenreListCommand;
import org.scriptorium.cli.commands.genre.GenreShowCommand;
import org.scriptorium.cli.commands.genre.GenreUpdateCommand;
import org.scriptorium.cli.commands.publisher.PublisherCreateCommand;
import org.scriptorium.cli.commands.publisher.PublisherDeleteCommand;
import org.scriptorium.cli.commands.publisher.PublisherListCommand;
import org.scriptorium.core.repositories.GenreRepository;
import org.scriptorium.core.repositories.JdbcGenreRepository;
import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.http.SimpleHttpClient;
import org.scriptorium.core.services.AuthorService;
import org.scriptorium.core.services.BookImportService;
import org.scriptorium.cli.commands.user.UserListCommand;
import org.scriptorium.cli.commands.user.UserCommand;
import org.scriptorium.cli.commands.user.UserCreateCommand;
import org.scriptorium.cli.commands.user.UserShowCommand;
import org.scriptorium.cli.commands.user.UserDeleteCommand;
import org.scriptorium.cli.commands.user.UserUpdateCommand;
import org.scriptorium.core.repositories.JdbcUserRepository;
import org.scriptorium.core.repositories.UserRepository;
import org.scriptorium.core.services.UserService;
import org.scriptorium.core.repositories.AuthorRepository;
import org.scriptorium.core.repositories.PublisherRepository;
import org.scriptorium.core.repositories.BookRepository;
import org.scriptorium.core.repositories.JdbcBookRepository;
import org.scriptorium.core.repositories.JdbcAuthorRepository;
import org.scriptorium.core.repositories.JdbcPublisherRepository;
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.services.PublisherService;

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
    private final BookFactory bookFactory;
    private final BookImportService bookImportService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;
    private final PublisherRepository publisherRepository;
    private final PublisherService publisherService;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final GenreRepository genreRepository;
    private final GenreService genreService;

    public DependencyFactory() {
        this.userRepository = new JdbcUserRepository("jdbc:sqlite:scriptorium.db");
        ((JdbcUserRepository) this.userRepository).init(); // Explicitly call init
        this.userService = new UserService(userRepository);

        this.authorRepository = new JdbcAuthorRepository("jdbc:sqlite:scriptorium.db");
        ((JdbcAuthorRepository) this.authorRepository).init(); // Explicitly call init
        this.authorService = new AuthorService(authorRepository);

        this.publisherRepository = new JdbcPublisherRepository("jdbc:sqlite:scriptorium.db");
        ((JdbcPublisherRepository) this.publisherRepository).init(); // Explicitly call init
        this.publisherService = new PublisherService(publisherRepository);

        this.genreRepository = new JdbcGenreRepository("jdbc:sqlite:scriptorium.db");
        ((JdbcGenreRepository) this.genreRepository).init(); // Explicitly call init
        this.genreService = new GenreService(genreRepository);

        this.bookRepository = new JdbcBookRepository("jdbc:sqlite:scriptorium.db", authorRepository, publisherRepository, genreRepository);
        ((JdbcBookRepository) this.bookRepository).init(); // Explicitly call init
        this.bookService = new BookService(bookRepository);

        this.bookFactory = new BookFactory(genreService);
        this.bookImportService = new BookImportService(httpClient, bookFactory, genreService);
    }

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
            return (K) new BookImportCommand(bookImportService, scanner, genreService);
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
        if (cls.isAssignableFrom(BookCreateCommand.class)) {
            return (K) new BookCreateCommand(bookService, genreService);
        }
        if (cls.isAssignableFrom(BookShowCommand.class)) {
            return (K) new BookShowCommand(bookService);
        }
        if (cls.isAssignableFrom(BookListCommand.class)) {
            return (K) new BookListCommand(bookService);
        }
        if (cls.isAssignableFrom(BookUpdateCommand.class)) {
            return (K) new BookUpdateCommand(bookService, genreService);
        }
        if (cls.isAssignableFrom(BookDeleteCommand.class)) {
            return (K) new BookDeleteCommand(bookService);
        }
        if (cls.isAssignableFrom(AuthorCommand.class)) {
            return (K) new AuthorCommand();
        }
        if (cls.isAssignableFrom(AuthorCreateCommand.class)) {
            return (K) new AuthorCreateCommand(authorService);
        }
        if (cls.isAssignableFrom(AuthorShowCommand.class)) {
            return (K) new AuthorShowCommand(authorService);
        }
        if (cls.isAssignableFrom(AuthorListCommand.class)) {
            return (K) new AuthorListCommand(authorService);
        }
        if (cls.isAssignableFrom(AuthorUpdateCommand.class)) {
            return (K) new AuthorUpdateCommand(authorService);
        }
        if (cls.isAssignableFrom(AuthorDeleteCommand.class)) {
            return (K) new AuthorDeleteCommand(authorService);
        }
        if (cls.isAssignableFrom(PublisherCreateCommand.class)) {
            return (K) new PublisherCreateCommand(publisherService);
        }
        if (cls.isAssignableFrom(PublisherDeleteCommand.class)) {
            return (K) new PublisherDeleteCommand(publisherService);
        }
        if (cls.isAssignableFrom(PublisherListCommand.class)) {
            return (K) new PublisherListCommand(publisherService);
        }

        if (cls.isAssignableFrom(GenreCommand.class)) {
            return (K) new GenreCommand();
        }
        if (cls.isAssignableFrom(GenreCreateCommand.class)) {
            return (K) new GenreCreateCommand(genreService);
        }
        if (cls.isAssignableFrom(GenreDeleteCommand.class)) {
            return (K) new GenreDeleteCommand(genreService);
        }
        if (cls.isAssignableFrom(GenreListCommand.class)) {
            return (K) new GenreListCommand(genreService);
        }
        if (cls.isAssignableFrom(GenreShowCommand.class)) {
            return (K) new GenreShowCommand(genreService);
        }
        if (cls.isAssignableFrom(GenreUpdateCommand.class)) {
            return (K) new GenreUpdateCommand(genreService);
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
