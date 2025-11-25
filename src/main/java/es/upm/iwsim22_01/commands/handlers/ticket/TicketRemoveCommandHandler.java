package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.commands.predicates.CheckIdInManagerPredicate;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;

public class TicketRemoveCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_REMOVE = "Incorrect use: ticket remove <ticketId> <cashId> <prodId>",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",

            TICKET_REMOVAL_OK = "ticket remove: ok";

    private final TicketManager ticketManager;
    private final ProductManager productManager;
    private final CashierManager cashierManager;

    public TicketRemoveCommandHandler(TicketManager ticketManager, ProductManager productManager, CashierManager cashierManager) {
        this.ticketManager = ticketManager;
        this.productManager = productManager;
        this.cashierManager = cashierManager;
    }

    @Override
    public void runCommand(CommandTokens tokens) {
        try {
            OptionalInt ticketId = tokens.nextInt(new CheckIdInManagerPredicate<>(ticketManager));
            if (ticketId.isEmpty()) {
                System.out.println(ERROR_TICKET_NOT_FOUND);
                return;
            }

            Optional<String> cashierId = tokens.next(new CheckIdInManagerPredicate<>(cashierManager));
            if (cashierId.isEmpty()) {
                System.out.println(ERROR_CASHIER_NOT_FOUND);
                return;
            }

            OptionalInt productId = tokens.nextInt(new CheckIdInManagerPredicate<>(productManager));
            if (productId.isEmpty()) {
                System.out.println(ERROR_PRODUCT_NOT_FOUND);
                return;
            }

            Product product = productManager.get(productId.getAsInt());
            Ticket ticket = ticketManager.get(ticketId.getAsInt());

            ticket.removeProduct(product);

            System.out.println(TICKET_REMOVAL_OK);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_REMOVE);
        }
    }
}
