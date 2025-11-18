package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.PersonalizableProduct;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

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
        //ticketId
        Integer ticketId = tokens.nextAsIntegerId(ticketManager, true, ERROR_INCORRECT_USE_TICKET_ADD, ERROR_TICKET_NOT_FOUND);
        if (ticketId == null) return;

        //cashierId
        String cashierId = tokens.nextAsStringId(cashierManager, true, ERROR_INCORRECT_USE_TICKET_ADD, ERROR_CASHIER_NOT_FOUND);
        if (cashierId == null) return;

        //productId
        Integer productId = tokens.nextAsIntegerId(productManager, true, ERROR_INCORRECT_USE_TICKET_ADD, ERROR_PRODUCT_NOT_FOUND);
        if (productId == null) return;

        //amount
        Integer amount = tokens.nextInRange(0, Ticket.MAX_PRODUCTS, ERROR_INCORRECT_USE_TICKET_ADD, ERROR_INVALID_AMOUNT);
        if (amount == null) return;

        Product product = productManager.get(productId);
        Ticket ticket = ticketManager.get(ticketId);

        //personalizableTexts
        if (tokens.hasNext()) {
            System.out.println(ERROR_INCORRECT_USE_TICKET_ADD);
            if (product instanceof PersonalizableProduct personalizableProduct) {
                ticket.addProduct(personalizableProduct, amount, tokens.next().split("--p"));
            } else {
                System.out.println(ERROR_PRODUCT_IS_NO_PERSONALIZABLE);
                return;
            }
        } else {
            ticket.addProduct(product, amount);
        }

        System.out.println(TICKET_ADD_OK);
    }
}
