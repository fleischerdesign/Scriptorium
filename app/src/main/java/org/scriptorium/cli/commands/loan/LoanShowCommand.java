package org.scriptorium.cli.commands.loan;

import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to display details of a specific loan by ID.
 * This command allows users to retrieve and view information about a loan
 * stored in the system.
 */
@Command(
    name = "show",
    description = "Displays details of a loan by ID."
)
public class LoanShowCommand implements Callable<Integer> {

    @ParentCommand
    LoanCommand parent; // Injects the parent command (LoanCommand)

    private final LoanService loanService;

    /**
     * Constructor for LoanShowCommand.
     * @param loanService The service responsible for loan operations.
     */
    public LoanShowCommand(LoanService loanService) {
        this.loanService = loanService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the loan to show", required = true)
    private Long id;

    /**
     * Executes the loan show command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Loan> loanOptional = loanService.findLoanById(id);

            if (loanOptional.isPresent()) {
                Loan loan = loanOptional.get();
                System.out.println("Loan Details:");
                System.out.println("ID: " + loan.getId());
                System.out.println("Copy: " + (loan.getCopy() != null ? loan.getCopy().getBarcode() + " (ID: " + loan.getCopy().getId() + ")" : "N/A"));
                System.out.println("User: " + (loan.getUser() != null ? loan.getUser().getFirstName() + " " + loan.getUser().getLastName() + " (ID: " + loan.getUser().getId() + ")" : "N/A"));
                System.out.println("Loan Date: " + loan.getLoanDate());
                System.out.println("Due Date: " + loan.getDueDate());
                System.out.println("Return Date: " + (loan.getReturnDate() != null ? loan.getReturnDate() : "Not returned"));
            } else {
                System.out.println("Loan with ID " + id + " not found.");
                return 1;
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving loan: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
