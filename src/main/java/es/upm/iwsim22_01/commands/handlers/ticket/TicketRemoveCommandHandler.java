package es.upm.iwsim22_01.commands.handlers.ticket;

import es.upm.iwsim22_01.commands.CommandTokens;
import es.upm.iwsim22_01.commands.handlers.CommandHandler;
import es.upm.iwsim22_01.manager.CashierManager;
import es.upm.iwsim22_01.manager.ProductManager;
import es.upm.iwsim22_01.manager.TicketManager;
import es.upm.iwsim22_01.models.Product;
import es.upm.iwsim22_01.models.Ticket;

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
        //ticketId
        Integer ticketId = tokens.nextAsIntegerId(ticketManager, true, ERROR_INCORRECT_USE_TICKET_REMOVE, ERROR_TICKET_NOT_FOUND);
        if (ticketId == null) return;

        //cashierId
        String cashierId = tokens.nextAsStringId(cashierManager, true, ERROR_INCORRECT_USE_TICKET_REMOVE, ERROR_CASHIER_NOT_FOUND);
        if (cashierId == null) return;

        //productId
        Integer productId = tokens.nextAsIntegerId(productManager, true, ERROR_INCORRECT_USE_TICKET_REMOVE, ERROR_PRODUCT_NOT_FOUND);
        if (productId == null) return;

        Product product = productManager.get(productId);
        Ticket ticket = ticketManager.get(ticketId);

        ticket.removeProduct(product);

        System.out.println(TICKET_REMOVAL_OK);
    }
}
