package org.scriptorium.cli.commands.loan;

import org.scriptorium.core.domain.Loan;
import org.scriptorium.core.services.LoanService;
import org.scriptorium.core.exceptions.DataAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Command to list all loans or loans filtered by user/book ID.
 * This command retrieves and displays a summary of loans currently
 * stored in the application's database.
 */
@Command(
    name = "list",
    description = "Lists all loans or loans filtered by user/book ID."
)
public class LoanListCommand implements Callable<Integer> {

    @ParentCommand
    LoanCommand parent; // Injects the parent command (LoanCommand)

    private final LoanService loanService;

    /**
     * Constructor for LoanListCommand.
     * @param loanService The service responsible for loan operations.
     */
    public LoanListCommand(LoanService loanService) {
        this.loanService = loanService;
    }

    @Option(names = {"-u", "--user-id"}, description = "Filter loans by user ID")
    private Long userId;

    @Option(names = {"-c", "--copy-id"}, description = "Filter loans by copy ID")
    private Long copyId;

    /**
     * Executes the loan list command.
     * @return 0 for success, 1 for failure.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public Integer call() throws Exception {
        try {
            List<Loan> loans;

            if (userId != null && copyId != null) {
                System.err.println("Error: Cannot provide both --user-id and --copy-id. Please choose one.");
                return 1;
            } else if (userId != null) {
                loans = loanService.findLoansByUserId(userId);
            } else if (copyId != null) {
                loans = loanService.findLoansByCopyId(copyId);
            } else {
                loans = loanService.findAll();
            }

            if (loans.isEmpty()) {
                System.out.println("No loans found.");
            } else {
                System.out.println("Listing loans:");
                System.out.printf("%-5s %-20s %-20s %-12s %-12s %-15s%n", "ID", "Copy Barcode", "User Name", "Loan Date", "Due Date", "Return Date");
                System.out.println("-----------------------------------------------------------------------------------------");
                for (Loan loan : loans) {
                    String copyBarcode = (loan.getCopy() != null) ? loan.getCopy().getBarcode() : "N/A";
                    String userName = (loan.getUser() != null) ? loan.getUser().getFirstName() + " " + loan.getUser().getLastName() : "N/A";
                    String returnDate = (loan.getReturnDate() != null) ? loan.getReturnDate().toString() : "Not returned";
                    System.out.printf("%-5d %-20s %-20s %-12s %-12s %-15s%n",
                        loan.getId(),
                        copyBarcode,
                        userName,
                        loan.getLoanDate(),
                        loan.getDueDate(),
                        returnDate
                    );
                }
            }
            return 0;
        } catch (DataAccessException e) {
            System.err.println("Error retrieving loans: " + e.getMessage());
            return 1;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
