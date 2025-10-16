package org.scriptorium.core.factories;

import org.scriptorium.api.controllers.CrudController;
import java.util.ArrayList;
import java.util.List;
import org.scriptorium.api.ApiServer;
import org.scriptorium.api.controllers.BookController;
import org.scriptorium.api.controllers.AuthorController;
import org.scriptorium.api.controllers.UserController;
import org.scriptorium.api.controllers.PublisherController;
import org.scriptorium.api.controllers.GenreController;
import org.scriptorium.api.controllers.CopyController;
import org.scriptorium.api.controllers.LoanController;
import org.scriptorium.api.controllers.ReservationController;
import org.scriptorium.config.ConfigLoader;
import org.scriptorium.cli.commands.server.ServerCommand;
import org.scriptorium.cli.commands.server.ServerStartCommand;
import org.scriptorium.cli.commands.server.ServerStopCommand;
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
import org.scriptorium.cli.commands.loan.LoanCommand;
import org.scriptorium.cli.commands.loan.LoanCreateCommand;
import org.scriptorium.cli.commands.loan.LoanDeleteCommand;
import org.scriptorium.cli.commands.loan.LoanListCommand;
import org.scriptorium.cli.commands.loan.LoanReturnCommand;
import org.scriptorium.cli.commands.loan.LoanShowCommand;
import org.scriptorium.cli.commands.reservation.ReservationCommand;
import org.scriptorium.cli.commands.reservation.ReservationCreateCommand;
import org.scriptorium.cli.commands.reservation.ReservationCancelCommand;
import org.scriptorium.cli.commands.reservation.ReservationFulfillCommand;
import org.scriptorium.cli.commands.reservation.ReservationListCommand;
import org.scriptorium.cli.commands.reservation.ReservationShowCommand;
import org.scriptorium.cli.commands.reservation.ReservationDeleteCommand;
import org.scriptorium.cli.commands.copy.CopyCommand;
import org.scriptorium.cli.commands.copy.CopyCreateCommand;
import org.scriptorium.cli.commands.copy.CopyListCommand;
import org.scriptorium.cli.commands.copy.CopyShowCommand;
import org.scriptorium.cli.commands.copy.CopyUpdateCommand;
import org.scriptorium.cli.commands.copy.CopyDeleteCommand;
import org.scriptorium.cli.commands.loan.LoanUpdateCommand;
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
import org.scriptorium.core.repositories.JdbcLoanRepository;
import org.scriptorium.core.repositories.LoanRepository;
import org.scriptorium.core.repositories.ReservationRepository;
import org.scriptorium.core.repositories.JdbcReservationRepository;
import org.scriptorium.core.repositories.CopyRepository; // New
import org.scriptorium.core.repositories.JdbcCopyRepository; // New
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.services.PublisherService;
import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.services.ReservationService;
import org.scriptorium.core.services.CopyService; // New

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
    private final ConfigLoader configLoader;
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
    private final LoanRepository loanRepository;
    private final LoanService loanService;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final CopyRepository copyRepository; // New
    private final CopyService copyService;     // New

    /**
     * Constructor for DependencyFactory. I'm initializing all my service and repository
     * instances here. This ensures that all dependencies are set up once and are ready
     * for injection into commands or the API server.
     */
    public DependencyFactory() {
        this.configLoader = new ConfigLoader("config.properties");
        String dbUrl = configLoader.getProperty("database.url", "jdbc:sqlite:scriptorium.db");

        this.userRepository = new JdbcUserRepository(dbUrl);
        ((JdbcUserRepository) this.userRepository).init(); // Explicitly call init
        this.userService = new UserService(userRepository);

        this.authorRepository = new JdbcAuthorRepository(dbUrl);
        ((JdbcAuthorRepository) this.authorRepository).init(); // Explicitly call init
        this.authorService = new AuthorService(authorRepository);

        this.publisherRepository = new JdbcPublisherRepository(dbUrl);
        ((JdbcPublisherRepository) this.publisherRepository).init(); // Explicitly call init
        this.publisherService = new PublisherService(publisherRepository);

        this.genreRepository = new JdbcGenreRepository(dbUrl);
        ((JdbcGenreRepository) this.genreRepository).init(); // Explicitly call init
        this.genreService = new GenreService(genreRepository);

        this.bookRepository = new JdbcBookRepository(dbUrl, authorRepository, publisherRepository, genreRepository);
        ((JdbcBookRepository) this.bookRepository).init(); // Explicitly call init
        this.bookService = new BookService(bookRepository);

        this.copyRepository = new JdbcCopyRepository(dbUrl, bookRepository); // Initialize CopyRepository
        ((JdbcCopyRepository) this.copyRepository).init(); // Explicitly call init
        this.copyService = new CopyService(copyRepository, bookRepository); // Initialize CopyService

        this.loanRepository = new JdbcLoanRepository(dbUrl, copyRepository, userRepository);
        ((JdbcLoanRepository) this.loanRepository).init(); // Explicitly call init
        this.loanService = new LoanService(loanRepository, copyRepository, userRepository);

        this.reservationRepository = new JdbcReservationRepository(dbUrl, bookRepository, userRepository);
        ((JdbcReservationRepository) this.reservationRepository).init(); // Explicitly call init
        this.reservationService = new ReservationService(reservationRepository, bookRepository, userRepository);

        this.bookFactory = new BookFactory(genreService);
        this.bookImportService = new BookImportService(httpClient, bookFactory, genreService);
    }

    /**
     * Creates and configures the ApiServer instance.
     * This method is responsible for wiring all the necessary controllers
     * into the ApiServer before returning it.
     * @return A fully configured ApiServer instance.
     */
    public ApiServer createApiServer() {
        ApiServer server = new ApiServer();
        List<CrudController<?, ?, ?>> controllers = new ArrayList<>();
        controllers.add(new BookController(bookService));
        controllers.add(new AuthorController(authorService));
        controllers.add(new UserController(userService));
        controllers.add(new PublisherController(publisherService));
        controllers.add(new GenreController(genreService));
        controllers.add(new CopyController(copyService));
        controllers.add(new LoanController(loanService));
        controllers.add(new ReservationController(reservationService));
        server.defineRoutes(controllers);
        return server;
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
            return (K) new BookImportCommand(bookImportService, scanner, genreService, bookService);
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
        if (cls.isAssignableFrom(LoanCommand.class)) {
            return (K) new LoanCommand();
        }
        if (cls.isAssignableFrom(LoanCreateCommand.class)) {
            return (K) new LoanCreateCommand(loanService, copyService, userService);
        }
        if (cls.isAssignableFrom(LoanShowCommand.class)) {
            return (K) new LoanShowCommand(loanService);
        }
        if (cls.isAssignableFrom(LoanListCommand.class)) {
            return (K) new LoanListCommand(loanService);
        }
        if (cls.isAssignableFrom(LoanUpdateCommand.class)) {
            return (K) new LoanUpdateCommand(loanService);
        }
        if (cls.isAssignableFrom(LoanDeleteCommand.class)) {
            return (K) new LoanDeleteCommand(loanService);
        }
        if (cls.isAssignableFrom(LoanReturnCommand.class)) {
            return (K) new LoanReturnCommand(loanService);
        }
        if (cls.isAssignableFrom(ReservationCommand.class)) {
            return (K) new ReservationCommand();
        }
        if (cls.isAssignableFrom(ReservationCreateCommand.class)) {
            return (K) new ReservationCreateCommand(reservationService, bookService, userService);
        }
        if (cls.isAssignableFrom(ReservationCancelCommand.class)) {
            return (K) new ReservationCancelCommand(reservationService);
        }
        if (cls.isAssignableFrom(ReservationFulfillCommand.class)) {
            return (K) new ReservationFulfillCommand(reservationService);
        }
        if (cls.isAssignableFrom(ReservationListCommand.class)) {
            return (K) new ReservationListCommand(reservationService);
        }
        if (cls.isAssignableFrom(ReservationShowCommand.class)) {
            return (K) new ReservationShowCommand(reservationService);
        }
        if (cls.isAssignableFrom(ReservationDeleteCommand.class)) {
            return (K) new ReservationDeleteCommand(reservationService);
        }
        if (cls.isAssignableFrom(CopyCommand.class)) {
            return (K) new CopyCommand();
        }
        if (cls.isAssignableFrom(CopyCreateCommand.class)) {
            return (K) new CopyCreateCommand(copyService, bookService);
        }
        if (cls.isAssignableFrom(CopyListCommand.class)) {
            return (K) new CopyListCommand(copyService);
        }
        if (cls.isAssignableFrom(CopyShowCommand.class)) {
            return (K) new CopyShowCommand(copyService, bookService);
        }
        if (cls.isAssignableFrom(CopyUpdateCommand.class)) {
            return (K) new CopyUpdateCommand(copyService);
        }
        if (cls.isAssignableFrom(CopyDeleteCommand.class)) {
            return (K) new CopyDeleteCommand(copyService);
        }
        if (cls.isAssignableFrom(ServerCommand.class)) {
            return (K) new ServerCommand();
        }
        if (cls.isAssignableFrom(org.scriptorium.cli.commands.server.ServerStartCommand.class)) {
            return (K) new org.scriptorium.cli.commands.server.ServerStartCommand();
        }
        if (cls.isAssignableFrom(org.scriptorium.cli.commands.server.ServerStopCommand.class)) {
            return (K) new org.scriptorium.cli.commands.server.ServerStopCommand();
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