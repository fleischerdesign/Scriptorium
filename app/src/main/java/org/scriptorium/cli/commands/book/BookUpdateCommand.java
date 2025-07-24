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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Command to update an existing book in the system.
 * This command allows users to modify details of a book identified by its ID.
 * It directly interacts with the Book entity's setters, applying updates only for provided options.
 */
@Command(
    name = "update",
    description = "Updates an existing book in the system."
)
public class BookUpdateCommand implements Callable<Integer> {

    @ParentCommand
    BookCommand parent; // Injects the parent command (BookCommand)

    private final BookService bookService;
    private final GenreService genreService;

    /**
     * Constructor for BookUpdateCommand.
     * @param bookService The service responsible for book operations.
     * @param genreService The service responsible for genre operations.
     */
    public BookUpdateCommand(BookService bookService, GenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the book to update", required = true)
    private Long id;

    @Option(names = {"-t", "--title"}, description = "New title of the book")
    private String title;

    @Option(names = {"-is", "--isbn"}, description = "New ISBN of the book")
    private String isbn;

    @Option(names = {"-p", "--publication-date"}, description = "New publication date (YYYY-MM-DD)")
    private String publicationDate;

    @Option(names = {"-a", "--author"}, description = "New author(s) of the book (comma-separated)")
    private String authorNamesInput; // Renamed to handle multiple authors

    @Option(names = {"-pub", "--publisher"}, description = "New publisher of the book")
    private String publisherName;

    @Option(names = {"-g", "--genre"}, description = "New genre of the book (e.g., FICTION, MYSTERY)")
    private String genre;

    /**
     * Executes the book update command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Book> existingBookOptional = bookService.findById(id);

            if (existingBookOptional.isEmpty()) {
                System.out.println("Book with ID " + id + " not found. Cannot update.");
                return 1;
            }

            Book existingBook = existingBookOptional.get();

            // Update fields only if new values are provided
            if (title != null) {
                existingBook.setTitle(title);
            }
            if (isbn != null) {
                existingBook.setIsbns(Collections.singletonList(isbn));
            }
            if (publicationDate != null) {
                existingBook.setPublicationYear(LocalDate.parse(publicationDate).getYear());
            }
            if (authorNamesInput != null) {
                List<Author> authors = Arrays.stream(authorNamesInput.split(","))
                                           .map(String::trim)
                                           .filter(name -> !name.isEmpty())
                                           .map(Author::new)
                                           .collect(Collectors.toList());
                existingBook.setAuthors(authors);
            }
            if (publisherName != null) {
                existingBook.setMainPublisher(new Publisher(publisherName));
            }
            if (genre != null) {
                Genre bookGenre = genreService.findGenreByName(genre)
                                            .orElseGet(() -> genreService.save(new Genre(genre)));
                existingBook.setGenre(bookGenre);
            }

            Book updatedBook = bookService.save(existingBook);

            System.out.println("Book updated successfully:");
            System.out.println("ID: " + updatedBook.getId());
            System.out.println("Title: " + updatedBook.getTitle());
            System.out.println("ISBNs: " + updatedBook.getIsbns());
            System.out.println("Publication Year: " + updatedBook.getPublicationYear());
            System.out.println("Authors: " + updatedBook.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "))); // Display all authors
            System.out.println("Publisher: " + updatedBook.getMainPublisher().getName());
            System.out.println("Genre: " + updatedBook.getGenre());

            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error updating book: " + e.getMessage());
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
