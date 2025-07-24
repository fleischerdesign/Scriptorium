package org.scriptorium.cli.commands.loan;

import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Command to update an existing loan in the system.
 * This command allows users to modify the due date or return date of a loan.
 */
@Command(
    name = "update",
    description = "Updates an existing loan."
)
public class LoanUpdateCommand implements Callable<Integer> {

    @ParentCommand
    LoanCommand parent; // Injects the parent command (LoanCommand)

    private final LoanService loanService;

    /**
     * Constructor for LoanUpdateCommand.
     * @param loanService The service responsible for loan operations.
     */
    public LoanUpdateCommand(LoanService loanService) {
        this.loanService = loanService;
    }

    @Option(names = {"-i", "--id"}, description = "ID of the loan to update", required = true)
    private Long id;

    @Option(names = {"-d", "--due-date"}, description = "New due date of the loan (YYYY-MM-DD)")
    private String newDueDateString;

    @Option(names = {"-r", "--return-date"}, description = "New return date of the loan (YYYY-MM-DD) or 'null' to clear")
    private String newReturnDateString;

    /**
     * Executes the loan update command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            Optional<Loan> existingLoanOptional = loanService.findById(id);

            if (existingLoanOptional.isEmpty()) {
                System.out.println("Loan with ID " + id + " not found. Cannot update.");
                return 1;
            }

            Loan existingLoan = existingLoanOptional.get();
            boolean updated = false;

            if (newDueDateString != null) {
                try {
                    LocalDate newDueDate = LocalDate.parse(newDueDateString);
                    existingLoan.setDueDate(newDueDate);
                    updated = true;
                } catch (DateTimeParseException e) {
                    System.err.println("Error: Invalid new due date format. Please use YYYY-MM-DD.");
                    return 1;
                }
            }

            if (newReturnDateString != null) {
                try {
                    if (newReturnDateString.equalsIgnoreCase("null")) {
                        existingLoan.setReturnDate(null);
                    } else {
                        LocalDate newReturnDate = LocalDate.parse(newReturnDateString);
                        existingLoan.setReturnDate(newReturnDate);
                    }
                    updated = true;
                } catch (DateTimeParseException e) {
                    System.err.println("Error: Invalid new return date format. Please use YYYY-MM-DD or 'null'.");
                    return 1;
                }
            }

            if (!updated) {
                System.out.println("No update parameters provided. Nothing to update for loan ID " + id + ".");
                return 0;
            }

            Loan updatedLoan = loanService.save(existingLoan);

            System.out.println("Loan updated successfully:");
            System.out.println("ID: " + updatedLoan.getId());
            System.out.println("Copy: " + (updatedLoan.getCopy() != null ? updatedLoan.getCopy().getBarcode() + " (ID: " + updatedLoan.getCopy().getId() + ")" : "N/A"));
            System.out.println("User: " + (updatedLoan.getUser() != null ? updatedLoan.getUser().getFirstName() + " " + updatedLoan.getUser().getLastName() + " (ID: " + updatedLoan.getUser().getId() + ")" : "N/A"));
            System.out.println("Loan Date: " + updatedLoan.getLoanDate());
            System.out.println("Due Date: " + updatedLoan.getDueDate());
            System.out.println("Return Date: " + (updatedLoan.getReturnDate() != null ? updatedLoan.getReturnDate() : "Not returned"));

            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error updating loan: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
