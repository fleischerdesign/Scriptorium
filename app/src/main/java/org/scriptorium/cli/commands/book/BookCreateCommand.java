package org.scriptorium.cli.commands.book;

import org.scriptorium.core.domain.Book;
import org.scriptorium.core.domain.Author;
import org.scriptorium.core.domain.Publisher;
import org.scriptorium.core.domain.Genre; // Import Genre
import org.scriptorium.core.services.BookService;
import org.scriptorium.core.services.GenreService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections; // For List.of() equivalent for single item
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Command to create a new book in the system.
 * This command allows users to add book details such as title, ISBN, publication date,
 * authors, publisher, and genre. It directly interacts with the Book entity's setters.
 */
@Command(
    name = "create",
    description = "Creates a new book in the system."
)
public class BookCreateCommand implements Callable<Integer> {

    @ParentCommand
    BookCommand parent; // Injects the parent command (BookCommand)

    private final BookService bookService;
    private final GenreService genreService;

    /**
     * Constructor for BookCreateCommand.
     * @param bookService The service responsible for book operations.
     * @param genreService The service responsible for genre operations.
     */
    public BookCreateCommand(BookService bookService, GenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @Option(names = {"-t", "--title"}, description = "Title of the book", required = true)
    private String title;

    @Option(names = {"-i", "--isbn"}, description = "ISBN of the book", required = true)
    private String isbn;

    @Option(names = {"-p", "--publication-date"}, description = "Publication date (YYYY-MM-DD)", required = true)
    private String publicationDate;

    @Option(names = {"-a", "--author"}, description = "Author(s) of the book (comma-separated)", required = true)
    private String authorNamesInput; // Renamed to avoid confusion with List<Author>

    @Option(names = {"-pub", "--publisher"}, description = "Publisher of the book", required = true)
    private String publisherName;

    @Option(names = {"-g", "--genre"}, description = "Genre of the book (e.g., FICTION, MYSTERY)", defaultValue = "UNKNOWN")
    private String genre;

    /**
     * Executes the book creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Book newBook = new Book();
            newBook.setTitle(title);
            newBook.setIsbns(Collections.singletonList(isbn)); // Convert single ISBN string to List<String>
            newBook.setPublicationYear(LocalDate.parse(publicationDate).getYear()); // Convert date string to year int
            newBook.setAuthors(Arrays.stream(authorNamesInput.split(","))
                                       .map(String::trim)
                                       .filter(name -> !name.isEmpty())
                                       .map(Author::new)
                                       .collect(Collectors.toList()));
            newBook.setMainPublisher(new Publisher(publisherName));
            // Handle Genre: Find existing or create new one
            Genre bookGenre = genreService.findGenreByName(genre)
                                        .orElseGet(() -> genreService.createGenre(new Genre(genre)));
            newBook.setGenre(bookGenre);
            newBook.setDescription(""); // Default description

            Book createdBook = bookService.createBook(newBook);

            System.out.println("Book created successfully:");
            System.out.println("ID: " + createdBook.getId());
            System.out.println("Title: " + createdBook.getTitle());
            System.out.println("ISBNs: " + createdBook.getIsbns());
            System.out.println("Publication Year: " + createdBook.getPublicationYear());
            System.out.println("Authors: " + createdBook.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "))); // Display all authors
            System.out.println("Publisher: " + createdBook.getMainPublisher().getName());
            System.out.println("Genre: " + createdBook.getGenre());

            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error creating book: " + e.getMessage());
            return 1;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
