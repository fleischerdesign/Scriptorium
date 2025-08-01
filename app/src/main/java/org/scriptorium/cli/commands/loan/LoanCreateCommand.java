package org.scriptorium.cli.commands.loan;

import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.services.UserService;
import org.scriptorium.core.services.CopyService; // New
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;

/**
 * Command to create a new loan in the system.
 * This command allows users to create a loan for a book to a user.
 */
@Command(
    name = "create",
    description = "Creates a new book loan."
)
public class LoanCreateCommand implements Callable<Integer> {

    @ParentCommand
    LoanCommand parent; // Injects the parent command (LoanCommand)

    private final LoanService loanService;
    private final CopyService copyService;
    private final UserService userService;

    /**
     * Constructor for LoanCreateCommand.
     * @param loanService The service responsible for loan operations.
     * @param bookService The service responsible for book operations.
     * @param userService The service responsible for user operations.
     */
    public LoanCreateCommand(LoanService loanService, CopyService copyService, UserService userService) {
        this.loanService = loanService;
        this.copyService = copyService;
        this.userService = userService;
    }

    @Option(names = {"-c", "--copy-id"}, description = "ID of the copy to loan", required = true)
    private Long copyId;

    @Option(names = {"-u", "--user-id"}, description = "ID of the user borrowing the book", required = true)
    private Long userId;

    @Option(names = {"-d", "--due-date"}, description = "Due date of the loan (YYYY-MM-DD)", required = true)
    private String dueDateString;

    /**
     * Executes the loan creation command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueDateString);
        } catch (DateTimeParseException e) {
            System.err.println("Error: Invalid due date format. Please use YYYY-MM-DD.");
            return 1;
        }

        try {
            // Optional: Verify book and user existence before creating loan
            // This is already handled by LoanService, but can add more specific messages here if needed
            copyService.findById(copyId)
                    .orElseThrow(() -> new IllegalArgumentException("Copy with ID " + copyId + " not found."));
            userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

            Loan newLoan = loanService.createLoan(copyId, userId, dueDate);

            System.out.println("Loan created successfully:");
            System.out.println("ID: " + newLoan.getId());
            System.out.println("Copy ID: " + newLoan.getCopy().getId());
            System.out.println("User ID: " + newLoan.getUser().getId());
            System.out.println("Loan Date: " + newLoan.getLoanDate());
            System.out.println("Due Date: " + newLoan.getDueDate());

            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error creating loan: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}