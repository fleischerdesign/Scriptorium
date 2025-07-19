package org.scriptorium.cli.commands.loan;

import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

/**
 * Command to delete a loan from the system by its ID.
 * This command allows users to remove a loan entry from the database.
 */
@Command(
    name = "delete",
    description = "Deletes a loan from the system by its ID."
)
public class LoanDeleteCommand implements Callable<Integer> {

    @ParentCommand
    LoanCommand parent; // Injects the parent command (LoanCommand)

    private final LoanService loanService;

    /**
     * Constructor for LoanDeleteCommand.
     * @param loanService The service responsible for loan operations.
     */
    public LoanDeleteCommand(LoanService loanService) {
        this.loanService = loanService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the loan to delete", required = true)
    private Long id;

    /**
     * Executes the loan deletion command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            // Check if the loan exists before attempting to delete
            if (loanService.findLoanById(id).isEmpty()) {
                System.out.println("Loan with ID " + id + " not found. No deletion performed.");
                return 1;
            }

            loanService.deleteLoan(id);
            System.out.println("Loan with ID " + id + " deleted successfully.");
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error deleting loan: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
