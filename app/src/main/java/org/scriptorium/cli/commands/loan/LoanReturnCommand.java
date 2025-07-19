package org.scriptorium.cli.commands.loan;

import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to mark a loan as returned in the system.
 * This command allows users to set the return date for an active loan.
 */
@Command(
    name = "return",
    description = "Marks a loan as returned."
)
public class LoanReturnCommand implements Callable<Integer> {

    @ParentCommand
    LoanCommand parent; // Injects the parent command (LoanCommand)

    private final LoanService loanService;

    /**
     * Constructor for LoanReturnCommand.
     * @param loanService The service responsible for loan operations.
     */
    public LoanReturnCommand(LoanService loanService) {
        this.loanService = loanService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the loan to return", required = true)
    private Long id;

    /**
     * Executes the loan return command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Loan returnedLoan = loanService.returnBook(id);

            System.out.println("Loan with ID " + id + " marked as returned successfully.");
            System.out.println("Return Date: " + returnedLoan.getReturnDate());

            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        } catch (DataAccessException e) {
            System.err.println("Error returning loan: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
