package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.commands.predicates.CheckIdInManagerPredicate;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.PersonalizableProduct;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;

public class TicketAddCommandHandler implements CommandHandler {
    private static final String
            ERROR_INCORRECT_USE_TICKET_ADD = "Incorrect use: ticket add <ticketId> <cashId> <prodId> <amount> [--p<txt> --p<txt>]",
            ERROR_PRODUCT_IS_NO_PERSONALIZABLE = "Product is not personalizable",
            ERROR_CASHIER_NOT_FOUND = "Cashier not found",
            ERROR_TICKET_NOT_FOUND = "Ticket not found",
            ERROR_INVALID_AMOUNT = "Invalid amount",
            ERROR_PRODUCT_NOT_FOUND = "Product not found",

            TICKET_ADD_OK = "ticket add: ok";

    private final TicketManager ticketManager;
    private final ProductManager productManager;
    private final CashierManager cashierManager;

    public TicketAddCommandHandler(TicketManager ticketManager, ProductManager productManager, CashierManager cashierManager) {
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

            OptionalInt amount = tokens.nextInt(integer -> integer > 0 && integer < Ticket.MAX_PRODUCTS);
            if (amount.isEmpty()) {
                System.out.println(ERROR_INVALID_AMOUNT);
                return;
            }

            Product product = productManager.get(productId.getAsInt());
            Ticket ticket = ticketManager.get(ticketId.getAsInt());

            //personalizableTexts
            if (tokens.hasNext()) {
                System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
                if (product instanceof PersonalizableProduct personalizableProduct) {
                    ticket.addProduct(personalizableProduct, amount.getAsInt(), tokens.next().split("--p"));
                } else {
                    System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                    return;
                }
            } else {
                ticket.addProduct(product, amount.getAsInt());
            }

            System.out.println(TICKET_ADD_OK);
        } catch (NoSuchElementException | IllegalArgumentException exception) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
        }
    }
}
